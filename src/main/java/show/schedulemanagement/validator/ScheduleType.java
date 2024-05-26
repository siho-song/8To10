package show.schedulemanagement.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ScheduleTypeValidator.class})
public @interface ScheduleType {
    String message() default "{schedule.type.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
