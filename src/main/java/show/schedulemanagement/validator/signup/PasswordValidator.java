package show.schedulemanagement.validator.signup;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordValidator implements ConstraintValidator<Password, String> {

    String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$";

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.debug("PasswordValidator :  value : {} , isValid : {} ", value,value != null && value.matches(PASSWORD_PATTERN));
        return value != null && value.matches(PASSWORD_PATTERN);
    }
}
