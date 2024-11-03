package com.example.budgetingapp.validation.date.todateafterfromdate;

import static com.example.budgetingapp.constants.validation.ValidationConstants.TO_DATE_EARLIER_THAN_FROM_DATE;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BudgetToDateAfterFromDateValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BudgetToDateAfterFromDate {
    String message() default TO_DATE_EARLIER_THAN_FROM_DATE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
