package com.example.budgetingapp.services.implementations;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.TARGET_QUANTITY_THRESHOLD;

import com.example.budgetingapp.dtos.targets.request.DeleteTargetRequestDto;
import com.example.budgetingapp.dtos.targets.request.ReplenishTargetRequestDto;
import com.example.budgetingapp.dtos.targets.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.targets.response.TargetTransactionResponseDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Target;
import com.example.budgetingapp.exceptions.conflictexpections.AlreadyExistsException;
import com.example.budgetingapp.exceptions.conflictexpections.ConflictException;
import com.example.budgetingapp.exceptions.conflictexpections.TransactionFailedException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TargetMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.target.TargetRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.interfaces.TargetService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TargetServiceImpl implements TargetService {
    private final UserRepository userRepository;
    private final TargetRepository targetRepository;
    private final AccountRepository accountRepository;
    private final TargetMapper targetMapper;

    @Override
    public TargetTransactionResponseDto saveTarget(Long userId,
                                               TargetTransactionRequestDto requestTransactionDto) {
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

    @Transactional
    @Override
    public TargetTransactionResponseDto replenishTarget(Long userId,
                                            ReplenishTargetRequestDto replenishTargetRequestDto) {
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
        if (target.getCurrentSum().compareTo(target.getExpectedSum()) >= 0) {
            target.setAchieved(true);
            target.setDownPayment(BigDecimal.ZERO);
        }
        return targetMapper.toTargetDto(targetRepository.save(target));
    }

    @Override
    public List<TargetTransactionResponseDto> getAllTargets(Long userId, Pageable pageable) {
        return targetRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(targetMapper::toTargetDto)
                .sorted(Comparator.comparing(
                        TargetTransactionResponseDto::getId))
                .toList();
    }

    @Transactional
    @Override
    public void deleteByTargetId(Long userId, DeleteTargetRequestDto deleteTargetRequestDto) {
        Target target = targetRepository.findByIdAndUserId(deleteTargetRequestDto.targetId(),
                userId).orElseThrow(() -> new EntityNotFoundException("No target with id "
                        + deleteTargetRequestDto.targetId() + " for user " + userId
                        + " was found"));
        Account account = accountRepository.findByIdAndUserId(deleteTargetRequestDto.accountId(),
                        userId).orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + deleteTargetRequestDto.accountId() + " for user " + userId
                        + " was found"));
        if (!account.getCurrency().equals(target.getCurrency())) {
            throw new IllegalArgumentException("You can't transfer money from target with currency "
                    + target.getCurrency() + " to account with currency " + account.getCurrency());
        }
        account.setBalance(account.getBalance().add(target.getCurrentSum()));
        accountRepository.save(account);
        targetRepository.deleteById(deleteTargetRequestDto.targetId());
    }

    private int isSufficientAmount(Account account, ReplenishTargetRequestDto requestDto) {
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
                                         TargetTransactionRequestDto targetTransactionRequestDto) {
        if (!accountRepository.existsByUserIdAndCurrency(
                userId, targetTransactionRequestDto.currency())) {
            throw new EntityNotFoundException("No account with currency "
                    + targetTransactionRequestDto.currency()
                    + " was found for user with id " + userId);
        }
    }
}
