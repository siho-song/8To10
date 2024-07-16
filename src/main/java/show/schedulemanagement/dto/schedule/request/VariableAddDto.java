package show.schedulemanagement.dto.schedule.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.validator.schedule.filedError.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objectError.StartBeforeEnd;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@StartBeforeEnd
@NoArgsConstructor
public class VariableAddDto extends ScheduleAddDto implements DateRangeValidatable{

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
