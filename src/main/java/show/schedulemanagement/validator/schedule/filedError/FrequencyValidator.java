package show.schedulemanagement.validator.schedule.filedError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FrequencyValidator implements ConstraintValidator<Frequency,String> {
    String SCHEDULE_FREQUENCY_PATTERN = "^(weekly|daily|biweekly)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(SCHEDULE_FREQUENCY_PATTERN);
    }
}
