package show.schedulemanagement.validator.schedule.objectError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.temporal.ChronoUnit;
import show.schedulemanagement.dto.schedule.request.nSchedule.NScheduleSave;

public class PerformInWeekValidator implements ConstraintValidator<PerformInWeek, NScheduleSave> {
    @Override
    public boolean isValid(NScheduleSave NScheduleSave, ConstraintValidatorContext constraintValidatorContext) {
        int performInWeek = NScheduleSave.getPerformInWeek();
        Boolean isIncludeSaturday = NScheduleSave.getIsIncludeSaturday();
        Boolean isIncludeSunday = NScheduleSave.getIsIncludeSunday();
        long between = ChronoUnit.DAYS.between(NScheduleSave.getStartDate(), NScheduleSave.getEndDate()) + 1;

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
