package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.transactions.request.FilterTransactionsDto;
import com.example.budgetingapp.dtos.transactions.request.RequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateRequestTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.helper.ChartTransactionRequestDtoByMonthOrYear;
import com.example.budgetingapp.dtos.transactions.response.ChartsAccumulatedResultDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseTransactionDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    SaveAndUpdateResponseTransactionDto saveTransaction(Long userId,
                                                    RequestTransactionDto requestTransactionDto);

    SaveAndUpdateResponseTransactionDto updateTransaction(Long userId,
                                                UpdateRequestTransactionDto requestTransactionDto,
                                                Long transactionId);

    GetTransactionsPageDto getAllTransactions(Long userId,
                                              FilterTransactionsDto transactionsDto,
                                              Pageable pageable);

    List<ChartsAccumulatedResultDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                             FilterTransactionsDto transactionsDto);

    List<ChartsAccumulatedResultDto> getSumOfTransactionsForMonthOrYear(Long userId,
                ChartTransactionRequestDtoByMonthOrYear chartTransactionRequestDtoByMonthOrYear);

    void deleteByTransactionId(Long userId, Long transactionId);
}
