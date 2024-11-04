package com.example.budgetingapp.validation.filtertype;

import static com.example.budgetingapp.constants.validation.ValidationConstants.INVALID_FILTER;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidFilterTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFilterType {
    String message() default INVALID_FILTER;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
