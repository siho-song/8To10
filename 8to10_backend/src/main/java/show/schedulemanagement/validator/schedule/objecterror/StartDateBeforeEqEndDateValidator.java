package show.schedulemanagement.validator.schedule.objecterror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;

public class StartDateBeforeEqEndDateValidator implements ConstraintValidator<StartDateBeforeEqEndDate, DateRangeValidatable> {
    @Override
    public boolean isValid(DateRangeValidatable dateRangeValidatable,
                           ConstraintValidatorContext constraintValidatorContext) {
        LocalDate startDate = dateRangeValidatable.takeStartDateTime().toLocalDate().minusDays(1);
        LocalDate endDate = dateRangeValidatable.takeEndDateTime().toLocalDate();

        return startDate.isBefore(endDate);
    }
}
