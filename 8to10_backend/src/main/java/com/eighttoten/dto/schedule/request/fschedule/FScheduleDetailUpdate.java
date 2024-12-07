package com.eighttoten.dto.schedule.request.fschedule;

import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;
import com.eighttoten.validator.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.validator.schedule.fielderror.ZeroSeconds;
import com.eighttoten.validator.schedule.objecterror.StartBeforeEnd;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@StartBeforeEnd(groups = ObjectErrorGroup.class)
public class FScheduleDetailUpdate implements DateRangeValidatable {
    @NotNull(groups = FieldErrorGroup.class)
    private Long id;

    private String detailDescription;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    private LocalDateTime startDate;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
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