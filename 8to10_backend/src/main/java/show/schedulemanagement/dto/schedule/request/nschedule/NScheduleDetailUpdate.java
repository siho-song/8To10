package show.schedulemanagement.dto.schedule.request.nschedule;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NScheduleDetailUpdate{
    @NotNull
    private Long id;
    private String detailDescription;
}