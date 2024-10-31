package com.example.budgetingapp.validation.date;

import static com.example.budgetingapp.constants.validation.ValidationConstants.DATE_BEFORE_TODAY;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateAfterTodayValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAfterToday {
    String message() default DATE_BEFORE_TODAY;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
