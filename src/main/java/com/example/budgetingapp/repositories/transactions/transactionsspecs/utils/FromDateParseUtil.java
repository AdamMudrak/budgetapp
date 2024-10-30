package com.example.budgetingapp.repositories.transactions.transactionsspecs.utils;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class FromDateParseUtil {
    private static final int[] YEAR_INDICES = new int[]{0, 4};
    private static final int[] MONTH_INDICES = new int[]{5, 7};
    private static final int DAY_INDEX = 8;
    private static final int DEFAULT_YEAR_MONTH_DAY_LENGTH = 0;
    private static final int YEAR_STRING_LENGTH = 4;
    private static final int YEAR_AND_MONTH_STRING_LENGTH = 7;
    private static final int FULL_DATE_STRING_LENGTH = 10;
    private static final int BASIC_MONTH = 1;
    private static final int BASIC_DAY = 1;

    public LocalDate parseDate(String[] params) {
        String dateString = params[0];
        try {
            switch (dateString.length()) {
                case DEFAULT_YEAR_MONTH_DAY_LENGTH -> {
                    LocalDate now = LocalDate.now();
                    return LocalDate.of(now.getYear(), now.getMonth(), BASIC_DAY);
                }
                case YEAR_STRING_LENGTH -> {
                    return LocalDate.of(Integer.parseInt(dateString),
                            BASIC_MONTH, BASIC_DAY);
                }
                case YEAR_AND_MONTH_STRING_LENGTH -> {
                    return LocalDate.of(Integer.parseInt(dateString
                                    .substring(YEAR_INDICES[0], YEAR_INDICES[1])),
                            Integer.parseInt(dateString.substring(MONTH_INDICES[0])), BASIC_DAY);
                }
                case FULL_DATE_STRING_LENGTH -> {
                    return LocalDate.of(
                            Integer.parseInt(dateString.substring(
                                    YEAR_INDICES[0], YEAR_INDICES[1])),
                            Integer.parseInt(dateString.substring(
                                    MONTH_INDICES[0], MONTH_INDICES[1])),
                            Integer.parseInt(dateString.substring(DAY_INDEX)));
                }
                default -> throw new IllegalArgumentException("transactionDate length should be "
                        + YEAR_STRING_LENGTH + ", "
                        + YEAR_AND_MONTH_STRING_LENGTH + " or "
                        + FULL_DATE_STRING_LENGTH
                        + " and have format in: 2024 / 2024-10 / 2024-10-31");
            }
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Can't process transactionDate:" + dateString
                    + "; Incorrect date format, should be: 2024 / 2024-10 / 2024-10-31");
        }
    }
}
