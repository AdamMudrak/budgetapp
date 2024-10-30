package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    ResponseTransactionDto saveTransaction(Long userId,
                                           RequestTransactionDto requestTransactionDto);

    ResponseTransactionDto updateTransaction(Long userId,
                                             RequestTransactionDto requestTransactionDto,
                                             Long transactionId);

    List<ResponseTransactionDto> getAllTransactions(Long userId,
                                                    FilterTransactionsDto transactionsDto,
                                                    Pageable pageable);

    void deleteByTransactionId(Long userId, Long transactionId);
}
