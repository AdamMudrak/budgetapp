package com.example.budgetingapp.services.implementations;

import com.example.budgetingapp.dtos.transfers.request.TransferRequestDto;
import com.example.budgetingapp.dtos.transfers.response.GetTransfersPageDto;
import com.example.budgetingapp.dtos.transfers.response.TransferResponseDto;
import com.example.budgetingapp.entities.Account;
import com.example.budgetingapp.entities.Transfer;
import com.example.budgetingapp.entities.User;
import com.example.budgetingapp.exceptions.conflictexpections.ConflictException;
import com.example.budgetingapp.exceptions.conflictexpections.TransactionFailedException;
import com.example.budgetingapp.exceptions.notfoundexceptions.EntityNotFoundException;
import com.example.budgetingapp.mappers.TransferMapper;
import com.example.budgetingapp.repositories.account.AccountRepository;
import com.example.budgetingapp.repositories.transfer.TransferRepository;
import com.example.budgetingapp.repositories.user.UserRepository;
import com.example.budgetingapp.services.implementations.transactions.TransactionsCommonFunctionsUtil;
import com.example.budgetingapp.services.interfaces.TransferService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransferMapper transferMapper;
    private final TransactionsCommonFunctionsUtil transactionsCommonFunctionsUtil;

    @Transactional
    @Override
    public TransferResponseDto transfer(Long userId,
                                        TransferRequestDto requestDto) {
        if (requestDto.fromAccountId().equals(requestDto.toAccountId())) {
            throw new ConflictException("You can't transfer to the same account you transfer from");
        }
        Account fromAccount = accountRepository.findByIdAndUserId(
                requestDto.fromAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + requestDto.fromAccountId()
                        + " was found for user with id " + userId));

        Account toAccount = accountRepository.findByIdAndUserId(
                        requestDto.toAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("No account with id "
                        + requestDto.toAccountId()
                        + " was found for user with id " + userId));
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            throw new IllegalArgumentException(
                    "You can't transfer between accounts with different currencies");
        }
        if (isSufficientAmount(fromAccount, requestDto) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        fromAccount.setBalance(fromAccount.getBalance()
                .subtract(requestDto.amount()));
        toAccount.setBalance(toAccount.getBalance().add(requestDto.amount()));

        Transfer transfer = transferMapper.toTransfer(requestDto);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No user with id " + userId + " found"));
        transfer.setUser(currentUser);
        transfer.setCurrency(toAccount.getCurrency());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return transferMapper.toTransferDto(transferRepository.save(transfer));
    }

    @Override
    public GetTransfersPageDto getAllTransfersByUserId(Long userId, Pageable pageable) {

        Page<Transfer> transferPage = transferRepository
                .findAllByUserIdPaged(userId, pageable);

        return new GetTransfersPageDto(transferPage.getNumber(),
                transferPage.getSize(),
                transferPage.getNumberOfElements(),
                transferPage.getTotalElements(),
                transferPage.getTotalPages(),
                transferMapper.toTransferDtoList(transferPage.getContent()));
    }

    @Transactional
    @Override
    public void deleteByTransferId(Long userId, Long transferId) {
        Transfer transfer = transferRepository.findByIdAndUserId(transferId, userId)
                .orElseThrow(() -> new EntityNotFoundException("No transfer with id " + transferId
                        + " was found for user with id " + userId));
        Account fromAccount = transfer.getFromAccount();
        Account toAccount = transfer.getToAccount();
        if (transactionsCommonFunctionsUtil
                .isSufficientAmount(toAccount, transfer.getAmount()) < 0) {
            throw new TransactionFailedException("Not enough money for transaction");
        }
        toAccount.setBalance(toAccount.getBalance().subtract(transfer.getAmount()));
        fromAccount.setBalance(fromAccount.getBalance().add(transfer.getAmount()));
        accountRepository.save(toAccount);
        accountRepository.save(fromAccount);
        transferRepository.deleteById(transferId);
    }

    private int isSufficientAmount(Account account, TransferRequestDto requestDto) {
        return (account.getBalance()
                .subtract(requestDto.amount()))
                .compareTo(BigDecimal.ZERO);
    }
}
