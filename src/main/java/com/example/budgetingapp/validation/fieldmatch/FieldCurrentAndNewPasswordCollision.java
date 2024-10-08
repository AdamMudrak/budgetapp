package com.example.budgetingapp.validation.fieldmatch;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldCurrentAndNewPasswordCollisionValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldCurrentAndNewPasswordCollision {
    String message() default "currentPassword collides with newPassword. Try again";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
