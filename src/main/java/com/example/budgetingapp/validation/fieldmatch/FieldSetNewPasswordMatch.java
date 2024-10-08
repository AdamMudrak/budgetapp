package com.example.budgetingapp.validation.fieldmatch;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldSetNewPasswordMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldSetNewPasswordMatch {
    String message() default "newPassword and repeatPassword don't match. Try again";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
