package com.example.budgetingapp.dtos.transactions.request;

import com.example.budgetingapp.constants.dtos.TransactionDtoConstants;
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
    @Schema(name = TransactionDtoConstants.COMMENT,
            example = TransactionDtoConstants.COMMENT_EXAMPLE)
    private String comment;
    @Schema(name = TransactionDtoConstants.AMOUNT,
            example = TransactionDtoConstants.AMOUNT_EXAMPLE)
    @Positive
    @Digits(integer = 9, fraction = 2)
    private BigDecimal amount;
    @Schema(name = TransactionDtoConstants.DATE,
            example = TransactionDtoConstants.DATE_EXAMPLE)
    @Date
    private String transactionDate;
    @Schema(name = TransactionDtoConstants.ACCOUNT_ID,
            example = TransactionDtoConstants.ACCOUNT_ID_EXAMPLE)
    @Positive
    private Long accountId;
    @Schema(name = TransactionDtoConstants.CATEGORY_ID,
            example = TransactionDtoConstants.CATEGORY_ID_EXAMPLE)
    @Positive
    private Long categoryId;
}
