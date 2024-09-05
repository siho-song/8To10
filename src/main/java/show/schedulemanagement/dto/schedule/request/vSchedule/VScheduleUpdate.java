package show.schedulemanagement.dto.schedule.request.vSchedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.validator.schedule.objectError.StartBeforeEnd;

@Data
@StartBeforeEnd
public class VScheduleUpdate implements DateRangeValidatable {
    private Long id;
    @NotBlank
    @Size(min = 1,max = 80)
    private String title;
    private String commonDescription;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
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
