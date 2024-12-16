package com.eighttoten.schedule.validator.fielderror;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {FrequencyValidator.class})
public @interface Frequency {
    String message() default "{schedule.frequency.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}