package com.eighttoten.schedule.validator.objecterror;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartDateBeforeEqEndDateValidator.class)
public @interface StartDateBeforeEqEndDate {
    String message() default "{schedule.startEqualBeforeEnd.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}