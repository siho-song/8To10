package com.eighttoten.schedule.validator.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ZeroTimeInfoValidator implements ConstraintValidator<ZeroTimeInfo, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if(localDateTime == null) {
            return true;
        }

        return  localDateTime.getHour() == 0 &&
                localDateTime.getMinute() == 0 &&
                localDateTime.getSecond() == 0 &&
                localDateTime.getNano() == 0;
    }
}
