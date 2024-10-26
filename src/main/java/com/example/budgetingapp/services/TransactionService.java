package com.example.budgetingapp.services;

import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    ResponseTransactionDto saveTransaction(RequestTransactionDto requestTransactionDto);

    ResponseTransactionDto updateTransaction(RequestTransactionDto requestTransactionDto,
                                             Long transactionId);

    List<ResponseTransactionDto> getAllTransactions(Pageable pageable);

    void deleteByTransactionId(Long transactionId, Long accountId);
}
