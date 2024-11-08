package show.schedulemanagement.dto.schedule.request.vschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.dto.schedule.request.ScheduleSave;
import show.schedulemanagement.validator.ValidationGroups.FieldErrorGroup;
import show.schedulemanagement.validator.ValidationGroups.ObjectErrorGroup;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@SuperBuilder
@ToString(callSuper = true)
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
