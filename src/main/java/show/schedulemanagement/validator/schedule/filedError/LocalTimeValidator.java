package show.schedulemanagement.validator.schedule.filederror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class LocalTimeValidator implements ConstraintValidator<ZeroSeconds , LocalTime> {
    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("null 일 수 없습니다.")
                    .addConstraintViolation();
            return false;
        }
        return value.getSecond() == 0;
    }
}
