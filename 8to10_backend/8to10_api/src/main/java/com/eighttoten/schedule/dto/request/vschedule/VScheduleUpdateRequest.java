package com.eighttoten.schedule.dto.request.vschedule;

import com.eighttoten.schedule.domain.vschedule.VScheduleUpdate;
import com.eighttoten.schedule.dto.request.DateRangeValidatable;
import com.eighttoten.schedule.validator.objecterror.StartBeforeEnd;
import com.eighttoten.support.ValidationGroups.FieldErrorGroup;
import com.eighttoten.support.ValidationGroups.ObjectErrorGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@StartBeforeEnd(groups = ObjectErrorGroup.class)
public class VScheduleUpdateRequest implements DateRangeValidatable {
    private Long id;

    @NotBlank(groups = FieldErrorGroup.class)
    @Size(min = 1,max = 80 , groups = FieldErrorGroup.class)
    private String title;

    private String commonDescription;

    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime startDateTime;

    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime endDateTime;

    public VScheduleUpdate toVScheduleUpdate(){
        return new VScheduleUpdate(id, title, commonDescription, startDateTime, endDateTime);
    }
    @Override
    public LocalDateTime takeStartDateTime() {
        return startDateTime;
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDateTime;
    }
}