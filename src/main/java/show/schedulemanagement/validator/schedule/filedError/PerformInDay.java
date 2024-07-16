package show.schedulemanagement.validator.schedule.filedError;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {PerformInDayValidator.class})
public @interface PerformInDay {
    String message() default "{schedule.performInDay.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
