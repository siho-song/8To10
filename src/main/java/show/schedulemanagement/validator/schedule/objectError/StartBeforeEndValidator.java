package show.schedulemanagement.validator.schedule.objectError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, DateRangeValidatable> {

    @Override
    public boolean isValid(DateRangeValidatable value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return value.takeStartDateTime().isBefore(value.takeEndDateTime());
    }
}
