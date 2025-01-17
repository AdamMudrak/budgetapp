package com.example.budgetingapp.services.interfaces;

import com.example.budgetingapp.dtos.transactions.request.CreateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.UpdateTransactionDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByDaysDto;
import com.example.budgetingapp.dtos.transactions.request.filters.FilterTransactionByMonthsYearsDto;
import com.example.budgetingapp.dtos.transactions.response.GetTransactionsPageDto;
import com.example.budgetingapp.dtos.transactions.response.SaveAndUpdateResponseDto;
import com.example.budgetingapp.dtos.transactions.response.charts.SumsByPeriodDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    SaveAndUpdateResponseDto saveTransaction(Long userId,
                                             CreateTransactionDto requestTransactionDto);

    SaveAndUpdateResponseDto updateTransaction(Long userId,
                                               UpdateTransactionDto requestTransactionDto,
                                               Long transactionId);

    GetTransactionsPageDto getAllTransactions(Long userId,
                                              FilterTransactionByDaysDto transactionsDto,
                                              Pageable pageable);

    List<SumsByPeriodDto> getSumOfTransactionsForPeriodOfTime(Long userId,
                                                       FilterTransactionByDaysDto transactionsDto);

    List<SumsByPeriodDto> getSumOfTransactionsForMonthOrYear(Long userId,
                      FilterTransactionByMonthsYearsDto chartTransactionRequestDtoByMonthOrYear);

    void deleteByTransactionId(Long userId, Long transactionId);
}
