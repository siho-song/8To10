package com.eighttoten.validator.schedule.objecterror;

import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, DateRangeValidatable> {

    @Override
    public boolean isValid(DateRangeValidatable value, ConstraintValidatorContext context) {
        return value.takeStartDateTime().isBefore(value.takeEndDateTime());
    }
}