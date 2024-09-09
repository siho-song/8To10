package show.schedulemanagement.validator.schedule.filederror;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {LocalDateTimeValidator.class, LocalTimeValidator.class})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZeroSeconds {
    String message() default "{schedule.zeroSecond.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
