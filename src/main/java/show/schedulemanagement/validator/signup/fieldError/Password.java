package show.schedulemanagement.validator.signup.fielderror;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {PasswordValidator.class})
public @interface Password {
    @NotBlank
    String message() default "{password.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
