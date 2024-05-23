package show.schedulemanagement.validator.signup;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username,String> {
    private static final String NAME_PATTERN = "^[가-힣]{2,6}$";

    @Override
    public void initialize(Username constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(NAME_PATTERN);
    }
}
