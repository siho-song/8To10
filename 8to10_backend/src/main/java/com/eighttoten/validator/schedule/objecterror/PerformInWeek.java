package com.eighttoten.validator.schedule.objecterror;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PerformInWeekValidator.class)
public @interface PerformInWeek {
    String message() default "{schedule.performInWeek.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
