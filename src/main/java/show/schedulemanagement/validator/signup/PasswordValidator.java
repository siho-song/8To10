package show.schedulemanagement.validator.signup;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password,String> {
    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() > 20 || value.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            return false;
        }
        int count = 0;
        if (value.matches(".*[A-Z]+.*")) count++;
        if (value.matches(".*[a-z]+.*")) count++;
        if (value.matches(".*\\d+.*")) count++;
        if (value.matches(".*[^A-Za-z0-9]+.*")) count++;
        return count >= 3;
    }
}
