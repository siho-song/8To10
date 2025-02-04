package com.eighttoten.schedule.validator.fielderror;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ZeroTimeInfoValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZeroTimeInfo {
    String message() default "{schedule.zeroTimeInfo.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}