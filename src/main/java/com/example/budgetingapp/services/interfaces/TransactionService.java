package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateRequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.AccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.ResponseTransactionDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    ResponseTransactionDto saveTransaction(Long userId,
                                           RequestTransactionDto requestTransactionDto);

    ResponseTransactionDto updateTransaction(Long userId,
                                             UpdateRequestTransactionDto requestTransactionDto,
                                             Long transactionId);

    List<ResponseTransactionDto> getAllTransactions(Long userId,
                                                    FilterTransactionsDto transactionsDto,
                                                    Pageable pageable);

    List<AccumulatedResultDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                           FilterTransactionsDto transactionsDto);

    List<AccumulatedResultDto> getSumOfTransactionsForMonthOrYear(Long userId,
                   ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear);

    void deleteByTransactionId(Long userId, Long transactionId);
}