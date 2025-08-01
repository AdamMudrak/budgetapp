package com.example.budgetingapp.services.impl.targets;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_QUANTITY_THRESHOLD;

import com.example.budgetingapp.dtos.targets.request.CreateTargetDto;
import com.example.budgetingapp.dtos.targets.request.DeleteTargetDto;
import com.example.budgetingapp.dtos.targets.request.ReplenishTargetDto;
import com.example.budgetingapp.dtos.targets.response.TargetDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Target;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.AlreadyExistsException;
import com.example.budgetingapp.exceptions.ConflictException;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.TransactionFailedException;
import com.example.budgetingapp.mappers.TargetMapper;
import com.example.budgetingapp.repositories.AccountRepository;
import com.example.budgetingapp.repositories.TargetRepository;
import com.example.budgetingapp.repositories.UserRepository;
import com.example.budgetingapp.repositories.transactions.ExpenseRepository;
import com.example.budgetingapp.repositories.transactions.IncomeRepository;
import com.example.budgetingapp.services.TargetService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TargetServiceImpl implements TargetService {
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final TargetRepository targetRepository;
    private final AccountRepository accountRepository;
    private final TargetMapper targetMapper;
    private final TargetTransactionsFactoryUtil targetTransactionsMapperUtil;

    @Override
    public TargetDto saveTarget(Long userId,
                                CreateTargetDto requestTransactionDto) {
        if (targetRepository.countTargetsByUserId(userId) >= TARGET_QUANTITY_THRESHOLD) {
            throw new ConflictException("You can't have more than " + TARGET_QUANTITY_THRESHOLD
                    + " targets!");
        }
        isAccountPresentByCurrencyAndUserId(userId, requestTransactionDto);
        if (targetRepository.existsByUserIdAndName(userId, requestTransactionDto.name())) {
            throw new AlreadyExistsException("You already have a target named "
                    + requestTransactionDto.name());
        }
        Target newTarget = targetMapper.toTarget(requestTransactionDto);
        newTarget.setUser(userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No user with id " + userId + " was found")));
        calculateDownPayment(newTarget);
        return targetMapper.toTargetDto(targetRepository.save(newTarget));
    }

    @Override
    public TargetDto replenishTarget(Long userId,
                                     ReplenishTargetDto replenishTargetRequestDto) {
        Account account = accountRepository.findByIdAndUserId(
                replenishTargetRequestDto.fromAccountId(), userId).orElseThrow(
                    () -> new EntityNotFoundException("No account with id "
                        + replenishTargetRequestDto.fromAccountId() + " for user " + userId
                        + " was found"));

        Target target = targetRepository.findByIdAndUserId(replenishTargetRequestDto.toTargetId(),
                userId).orElseThrow(() -> new EntityNotFoundException("No target with id "
                + replenishTargetRequestDto.toTargetId() + " for user " + userId
                + " was found"));

        if (!account.getCurrency().equals(target.getCurrency())) {
            throw new IllegalArgumentException(
                    "You can't transfer money from account with currency " + account.getCurrency()
                            + " to target with currency " + target.getCurrency());
        }

        if (isSufficientAmount(account, replenishTargetRequestDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        account.setBalance(account.getBalance()
                .subtract(replenishTargetRequestDto.sumOfReplenishment()));
        target.setCurrentSum(target.getCurrentSum()
                .add(replenishTargetRequestDto.sumOfReplenishment()));
        accountRepository.save(account);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No user with id " + userId + " was found"));

        expenseRepository.save(targetTransactionsMapperUtil
                .formExpense(replenishTargetRequestDto, account, user, target.getName()));

        if (target.getCurrentSum().compareTo(target.getExpectedSum()) >= 0) {
            target.setAchieved(true);
            target.setDownPayment(BigDecimal.ZERO);
        }
        return targetMapper.toTargetDto(targetRepository.save(target));
    }

    @Override
    public List<TargetDto> getAllTargets(Long userId) {
        return targetMapper.toTargetDtoList(targetRepository.findAllByUserId(userId));
    }

    @Override
    public void deleteByTargetId(Long userId, DeleteTargetDto deleteTargetRequestDto) {
        Target target = targetRepository.findByIdAndUserId(deleteTargetRequestDto.targetId(),
                userId).orElseThrow(() -> new EntityNotFoundException("No target with id "
                        + deleteTargetRequestDto.targetId() + " for user " + userId
                        + " was found"));
        Account account = accountRepository.findByIdAndUserId(deleteTargetRequestDto.accountId(),
                        userId).orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + deleteTargetRequestDto.accountId() + " for user " + userId
                        + " was found"));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No user with id " + userId + " was found"));

        if (!account.getCurrency().equals(target.getCurrency())) {
            throw new IllegalArgumentException("You can't transfer money from target with currency "
                    + target.getCurrency() + " to account with currency " + account.getCurrency());
        }
        incomeRepository.save(targetTransactionsMapperUtil.formIncome(target, account, user));
        account.setBalance(account.getBalance().add(target.getCurrentSum()));
        accountRepository.save(account);
        targetRepository.deleteById(deleteTargetRequestDto.targetId());
    }

    private int isSufficientAmount(Account account, ReplenishTargetDto requestDto) {
        return (account.getBalance()
                .subtract(requestDto.sumOfReplenishment()))
                .compareTo(BigDecimal.ZERO);
    }

    private void calculateDownPayment(Target target) {
        BigDecimal difference = target.getExpectedSum().subtract(target.getCurrentSum());
        long monthsToAchieve = ChronoUnit.MONTHS.between(LocalDate.now(),
                target.getAchievedBefore());
        if (monthsToAchieve > 0) {
            target.setDownPayment(difference.divide(
                    BigDecimal.valueOf(monthsToAchieve), RoundingMode.CEILING));
        } else {
            long daysToAchieve = ChronoUnit.DAYS.between(LocalDate.now(),
                    target.getAchievedBefore());
            target.setDownPayment(difference.divide(BigDecimal.valueOf(daysToAchieve),
                    RoundingMode.CEILING));
        }

    }

    private void isAccountPresentByCurrencyAndUserId(Long userId,
                                         CreateTargetDto targetTransactionRequestDto) {
        if (!accountRepository.existsByUserIdAndCurrency(
                userId, targetTransactionRequestDto.currency())) {
            throw new EntityNotFoundException("No account with currency "
                    + targetTransactionRequestDto.currency()
                    + " was found for user with id " + userId);
        }
    }
}
