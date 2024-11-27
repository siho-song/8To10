package com.eighttoten.dto.schedule.request.vschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import com.eighttoten.dto.schedule.request.ScheduleSave;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;
import com.eighttoten.validator.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.validator.schedule.fielderror.ZeroSeconds;
import com.eighttoten.validator.schedule.objecterror.StartBeforeEnd;

@SuperBuilder
@Getter
@StartBeforeEnd(groups = ObjectErrorGroup.class)
@NoArgsConstructor
public class VScheduleSave extends ScheduleSave implements DateRangeValidatable {

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