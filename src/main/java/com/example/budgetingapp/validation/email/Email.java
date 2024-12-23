package com.example.budgetingapp.validation.email;

import static com.example.budgetingapp.constants.validation.ValidationConstants.INVALID_EMAIL;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = EmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
    String message() default INVALID_EMAIL;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
