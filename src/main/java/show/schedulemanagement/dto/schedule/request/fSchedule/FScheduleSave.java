package show.schedulemanagement.dto.schedule.request.fSchedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.dto.schedule.request.ScheduleSave;
import show.schedulemanagement.validator.schedule.objectError.StartBeforeEnd;

@SuperBuilder
@Getter
@StartBeforeEnd
@NoArgsConstructor
@ToString(callSuper = true)
@Slf4j
public class FScheduleSave extends ScheduleSave implements DateRangeValidatable {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @NotNull
    private List<FScheduleDetailSave> events;

    @Override
    public LocalDateTime takeStartDateTime() {
        return startDate.atStartOfDay();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDate.atStartOfDay();
    }
}