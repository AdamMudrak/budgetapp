package com.example.budgetingapp.dtos.transfers.response;

import java.util.List;

public record GetTransfersPageDto(int pageNumber,
                                  int pageSize,
                                  int elementsPresentOnPage,
                                  long totalElements,
                                  int totalPages,
                                  List<TransferResponseDto> transactionsPageDtoList){}
