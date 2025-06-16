package com.example.budgetingapp.dtos.transactions.request;

import com.example.budgetingapp.validation.date.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTransactionDto {
    @Schema(name = "comment",
            example = "new transaction")
    private String comment;
    @Schema(name = "amount",
            example = "999.99")
    @Positive
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;
    @Schema(name = "transactionDate",
            example = "2024-10-29")
    @Date
    private String transactionDate;
    @Schema(name = "accountId",
            example = "1")
    @Positive
    private Long accountId;
    @Schema(name = "categoryId",
            example = "1")
    @Positive
    private Long categoryId;
}
