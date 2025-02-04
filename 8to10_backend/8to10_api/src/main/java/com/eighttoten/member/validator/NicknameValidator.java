package com.eighttoten.member.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname,String> {
    private static final String NICKNAME_PATTERN = "^[가-힣a-zA-Z0-9_.]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }

        if (!value.matches(NICKNAME_PATTERN)) {
            return false;
        }

        if (value.matches(".*[가-힣]+.*")) {
            return value.length() >= 2 && value.length() <= 12;
        } else {
            return value.length() <= 20;
        }
    }
}