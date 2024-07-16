package show.schedulemanagement.validator.schedule.filedError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class LocalDateTimeValidator implements ConstraintValidator<ZeroSeconds , LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("null 일 수 없습니다.")
                    .addConstraintViolation();
            return false;
        }
        return value.getSecond() == 0 && value.getNano() == 0;
    }
}
