package com.eighttoten.support;

import com.eighttoten.support.ValidationGroups.FieldErrorGroup;
import com.eighttoten.support.ValidationGroups.ObjectErrorGroup;
import jakarta.validation.GroupSequence;

@GroupSequence(value = {FieldErrorGroup.class, ObjectErrorGroup.class})
public interface ValidationSequence {
}