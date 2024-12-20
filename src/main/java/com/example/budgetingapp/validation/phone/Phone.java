package com.example.budgetingapp.validation.phone;

import static com.example.budgetingapp.constants.validation.ValidationConstants.INVALID_PHONE;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = PhoneValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default INVALID_PHONE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
