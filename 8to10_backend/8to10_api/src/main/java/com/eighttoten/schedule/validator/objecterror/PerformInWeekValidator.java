package com.eighttoten.schedule.validator.objecterror;

import com.eighttoten.schedule.dto.request.NScheduleSaveRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.temporal.ChronoUnit;

public class PerformInWeekValidator implements ConstraintValidator<PerformInWeek, NScheduleSaveRequest> {
    @Override
    public boolean isValid(NScheduleSaveRequest nScheduleSaveRequest, ConstraintValidatorContext constraintValidatorContext) {
        int performInWeek = nScheduleSaveRequest.getPerformInWeek();
        Boolean isIncludeSaturday = nScheduleSaveRequest.getIsIncludeSaturday();
        Boolean isIncludeSunday = nScheduleSaveRequest.getIsIncludeSunday();
        long between = ChronoUnit.DAYS.between(nScheduleSaveRequest.getStartDateTime(), nScheduleSaveRequest.getEndDateTime()) + 1;

        if(performInWeek >= 6 && areNotIncludeWeekend(isIncludeSaturday,isIncludeSunday)){
            if (performInWeek > 7) {
                return false;
            } else if (performInWeek == 7) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                                "{schedule.performInWeek.weekend.message}")
                        .addConstraintViolation();
                return false;
            }
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "{schedule.performInWeek.weekendDay.message}")
                    .addConstraintViolation();
            return false;
        }

        if(performInWeek > between){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("{schedule.performInWeek.less.message}")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean areNotIncludeWeekend(Boolean isIncludeSaturday, Boolean isIncludeSunday){
        return !isIncludeSaturday && !isIncludeSunday;
    }
}