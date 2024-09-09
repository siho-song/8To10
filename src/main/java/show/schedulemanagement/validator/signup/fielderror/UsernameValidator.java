package show.schedulemanagement.validator.signup.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernameValidator implements ConstraintValidator<Username,String> {
    private static final String NAME_PATTERN = "^[가-힣]{2,6}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.debug("UsernameValidator value : {} ", value);
        return value != null && value.matches(NAME_PATTERN);
    }
}
