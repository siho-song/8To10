package com.eighttoten.schedule.validator.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class LocalTimeValidator implements ConstraintValidator<ZeroSeconds , LocalTime> {
    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return value.getSecond() == 0;
    }
}