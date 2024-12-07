package com.eighttoten.validator;

import jakarta.validation.GroupSequence;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;
import com.eighttoten.validator.ValidationGroups.ObjectErrorGroup;

@GroupSequence(value = {FieldErrorGroup.class, ObjectErrorGroup.class})
public interface ValidationSequence {
}