package com.eighttoten.validator.schedule.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class PerformInDayValidator implements ConstraintValidator<PerformInDay, LocalTime> {
    @Override
    public boolean isValid(LocalTime performInDay, ConstraintValidatorContext constraintValidatorContext) {
        if(performInDay == null){
            return true;
        }
        return performInDay.isAfter(LocalTime.of(0, 0));
    }
}
