package show.schedulemanagement.dto.schedule.request.vschedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.validator.ValidationGroups.FieldErrorGroup;
import show.schedulemanagement.validator.ValidationGroups.ObjectErrorGroup;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@Data
@StartBeforeEnd(groups = ObjectErrorGroup.class)
public class VScheduleUpdate implements DateRangeValidatable {
    private Long id;

    @NotBlank(groups = FieldErrorGroup.class)
    @Size(min = 1,max = 80 , groups = FieldErrorGroup.class)
    private String title;

    private String commonDescription;

    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime startDate;

    @NotNull(groups = FieldErrorGroup.class)
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
