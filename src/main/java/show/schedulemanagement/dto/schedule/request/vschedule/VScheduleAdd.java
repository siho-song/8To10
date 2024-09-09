package show.schedulemanagement.dto.schedule.request.vschedule;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.dto.schedule.request.ScheduleSave;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@StartBeforeEnd
@NoArgsConstructor
public class VScheduleAdd extends ScheduleSave implements DateRangeValidatable {

    @ZeroSeconds
    private LocalDateTime start;
    @ZeroSeconds
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
