package com.eighttoten.schedule.validator.fielderror;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueDayListValidator implements ConstraintValidator<UniqueDayList, List<String>> {
    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Set<String> uniqueElements = new HashSet<>(value);
        return uniqueElements.size() == value.size();
    }
}