package show.schedulemanagement.validator.signup.fieldError;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber,String> {

    String PHONE_NUMBER_PATTERN = "^(01[016789]{1})[0-9]{3,4}[0-9]{4}$";
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(PHONE_NUMBER_PATTERN);
    }
}

