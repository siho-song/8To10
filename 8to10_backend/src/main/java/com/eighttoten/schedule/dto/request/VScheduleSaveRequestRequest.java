package com.eighttoten.schedule.dto.request;

import com.eighttoten.global.ValidationGroups.FieldErrorGroup;
import com.eighttoten.global.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.objecterror.StartBeforeEnd;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@StartBeforeEnd(groups = ObjectErrorGroup.class)
@NoArgsConstructor
public class VScheduleSaveRequestRequest extends ScheduleSaveRequest implements DateRangeValidatable {

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime start;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime end;

    @Override
    public LocalDateTime takeStartDateTime() {
        return start;
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return end;
    }
}