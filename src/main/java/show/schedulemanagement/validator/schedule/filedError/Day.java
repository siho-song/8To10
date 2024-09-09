package show.schedulemanagement.validator.schedule.filederror;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DayValidator.class})
public @interface Day {
    String message() default "{schedule.day.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
