package com.example.budgetingapp.dtos.accounts.response;

import java.math.BigDecimal;

public record AccountDto(Long id,
                         String name,
                         BigDecimal balance,
                         String currency,
                         boolean byDefault) {
}
