package show.schedulemanagement.validator.schedule.filedError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ScheduleTypeValidator implements ConstraintValidator<ScheduleType,String> {

    String SCHEDULE_TYPE_PATTERN = "^(variable|fixed|normal)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(SCHEDULE_TYPE_PATTERN);
    }
}

