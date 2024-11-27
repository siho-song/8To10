package com.eighttoten.validator.schedule.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DayValidator implements ConstraintValidator<Day,String> {
    String SCHEDULE_DAY_PATTERN = "^(mo|tu|we|th|fr|sa|su)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        return value.matches(SCHEDULE_DAY_PATTERN);
    }
}
