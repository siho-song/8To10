package show.schedulemanagement.validator;

import jakarta.validation.GroupSequence;
import show.schedulemanagement.validator.ValidationGroups.FieldErrorGroup;
import show.schedulemanagement.validator.ValidationGroups.ObjectErrorGroup;

@GroupSequence(value = {FieldErrorGroup.class, ObjectErrorGroup.class})
public interface ValidationSequence {
}
