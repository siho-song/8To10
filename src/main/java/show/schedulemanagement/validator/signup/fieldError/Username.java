package show.schedulemanagement.validator.signup.fieldError;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {UsernameValidator.class})
public @interface Username {
    String message() default "{username.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
