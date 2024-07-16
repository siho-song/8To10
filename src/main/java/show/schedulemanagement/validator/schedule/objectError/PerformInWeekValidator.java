package show.schedulemanagement.validator.schedule.objectError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.temporal.ChronoUnit;
import show.schedulemanagement.dto.schedule.request.NormalAddDto;

public class PerformInWeekValidator implements ConstraintValidator<PerformInWeek, NormalAddDto> {
    @Override
    public boolean isValid(NormalAddDto normalAddDto, ConstraintValidatorContext constraintValidatorContext) {
        int performInWeek = normalAddDto.getPerformInWeek();
        Boolean isIncludeSaturday = normalAddDto.getIsIncludeSaturday();
        Boolean isIncludeSunday = normalAddDto.getIsIncludeSunday();
        long between = ChronoUnit.DAYS.between(normalAddDto.getStartDate(), normalAddDto.getEndDate()) + 1;

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
