package com.eighttoten.dto.schedule.request.vschedule;

import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;
import com.eighttoten.validator.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.validator.schedule.objecterror.StartBeforeEnd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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