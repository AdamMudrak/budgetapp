package com.example.budgetingapp.services.impl.transactions;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Transfer;
import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.exceptions.TransactionFailedException;
import com.example.budgetingapp.mappers.TransferMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transfer.TransferRepository;
import com.example.budgetingapp.services.TransferService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final TransferMapper transferMapper;

    @Transactional
    @Override
    public TransferResponseDto transfer(Long userId,
                                        TransferRequestDto requestDto) {
        Account fromAccount = accountRepository.findByIdAndUserId(
                requestDto.getFromAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + requestDto.getFromAccountId()
                        + " was found for user with id " + userId));

        Account toAccount = accountRepository.findByIdAndUserId(
                        requestDto.getToAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + requestDto.getToAccountId()
                        + " was found for user with id " + userId));
        if (isSufficientAmount(fromAccount, requestDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        fromAccount.setBalance(fromAccount.getBalance()
                .subtract(requestDto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(requestDto.getAmount()));

        Transfer transfer = transferMapper.toTransfer(requestDto);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return transferMapper.toTransferDto(transferRepository.save(transfer));
    }

    private int isSufficientAmount(Account account, TransferRequestDto requestDto) {
        return (account.getBalance()
                .subtract(requestDto.getAmount()))
                .compareTo(BigDecimal.ZERO);
    }
}
