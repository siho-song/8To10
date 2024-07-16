package show.schedulemanagement.validator.schedule.objectError;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartEqualBeforeEndValidator.class)
public @interface StartEqualBeforeEnd {
    String message() default "{schedule.startEqualBeforeEnd.message}}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
