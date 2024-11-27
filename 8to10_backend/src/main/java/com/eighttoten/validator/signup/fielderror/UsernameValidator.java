package com.eighttoten.validator.signup.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username,String> {
    private static final String NAME_PATTERN = "^[가-힣]{2,6}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return value.matches(NAME_PATTERN);
    }
}