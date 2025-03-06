package com.example.budgetingapp.dtos.transactions.response;

import java.util.List;

public record GetTransactionsPageDto(int pageNumber,
                                     int pageSize,
                                     int elementsPresentOnPage,
                                     long totalElements,
                                     int totalPages,
                                     List<GetTransactionDto> transactionsPageDtoList){}
