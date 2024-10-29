package com.example.budgetingapp.dtos.transactions.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
import com.example.budgetingapp.validation.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestTransactionDto {
    @Schema(name = TransactionDtoConstants.COMMENT,
            example = TransactionDtoConstants.COMMENT_EXAMPLE)
    private String comment;
    @Schema(name = TransactionDtoConstants.AMOUNT,
            example = TransactionDtoConstants.AMOUNT_EXAMPLE,
            requiredMode = REQUIRED)
    @NotNull
    @Positive
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;
    @Schema(name = TransactionDtoConstants.DATE,
            example = TransactionDtoConstants.DATE_EXAMPLE,
            requiredMode = REQUIRED)
    @Date
    private String transactionDate;
    @NotNull
    @Positive
    @Digits(integer = 9, fraction = 0)
    private Long accountId;
    @NotNull
    @Positive
    @Digits(integer = 9, fraction = 0)
    private Long categoryId;
}
