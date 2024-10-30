package com.example.budgetingapp.dtos.transfers.request;

import java.math.BigDecimal;

public record ReplenishTargetRequestDto(Long fromAccountId,
                                        Long toTargetId,
                                        BigDecimal sumOfReplenishment) {
}
