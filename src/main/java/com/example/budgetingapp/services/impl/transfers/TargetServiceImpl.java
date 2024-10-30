package com.example.budgetingapp.services.impl.transfers;

import com.example.budgetingapp.dtos.transfers.request.DeleteTargetRequestDto;
import com.example.budgetingapp.dtos.transfers.request.TargetTransactionRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TargetTransactionResponseDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.transfers.Target;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TargetMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transfer.TargetRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.TargetService;
import jakarta.transaction.Transactional;
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
        Target newTarget = targetMapper.toTarget(requestTransactionDto);
        newTarget.setUser(userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No user with id " + userId + " was found")));
        return targetMapper.toTargetDto(targetRepository.save(newTarget));
    }

    @Override
    public List<TargetTransactionResponseDto> getAllTargets(Long userId, Pageable pageable) {
        return targetRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(targetMapper::toTargetDto)
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
}
