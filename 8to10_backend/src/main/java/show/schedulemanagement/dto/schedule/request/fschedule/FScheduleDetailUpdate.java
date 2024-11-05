package show.schedulemanagement.dto.schedule.request.fschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@Data
@Builder
@StartBeforeEnd
public class FScheduleDetailUpdate implements DateRangeValidatable {
    @NotNull
    private Long id;
    private String detailDescription;
    @NotNull
    @ZeroSeconds
    private LocalDateTime startDate;
    @NotNull
    @ZeroSeconds
    private LocalDateTime endDate;

    @Override
    public LocalDateTime takeStartDateTime() {
        return startDate;
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDate;
    }
}