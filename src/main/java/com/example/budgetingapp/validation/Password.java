package com.example.budgetingapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Password should contain"
            + " 1 lowercase letter,"
            + " 1 uppercase letter,"
            + " 1 digit,"
            + " 1 special character,"
            + " and be 8 to 32 characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
