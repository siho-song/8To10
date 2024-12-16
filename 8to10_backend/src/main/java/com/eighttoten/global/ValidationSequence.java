package com.eighttoten.global;

import com.eighttoten.global.ValidationGroups.FieldErrorGroup;
import com.eighttoten.global.ValidationGroups.ObjectErrorGroup;
import jakarta.validation.GroupSequence;

@GroupSequence(value = {FieldErrorGroup.class, ObjectErrorGroup.class})
public interface ValidationSequence {
}