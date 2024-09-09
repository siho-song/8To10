package show.schedulemanagement.dto.schedule.request.fSchedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.validator.schedule.filedError.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objectError.StartBeforeEnd;

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