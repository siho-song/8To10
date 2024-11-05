package show.schedulemanagement.dto.schedule.request.nschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@Data
@Builder
public class NScheduleDetailUpdate{
    @NotNull
    private Long id;
    private String detailDescription;
}