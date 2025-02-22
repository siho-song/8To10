package com.eighttoten.schedule.dto.request.fschedule;

import com.eighttoten.schedule.domain.fschedule.FDetailUpdate;
import com.eighttoten.schedule.dto.request.DateRangeValidatable;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.objecterror.StartBeforeEnd;
import com.eighttoten.support.ValidationGroups.FieldErrorGroup;
import com.eighttoten.support.ValidationGroups.ObjectErrorGroup;
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
public class FixDetailUpdateRequest implements DateRangeValidatable {
    @NotNull(groups = FieldErrorGroup.class)
    private Long id;

    private String detailDescription;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    private LocalDateTime startDateTime;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    private LocalDateTime endDateTime;

    public FDetailUpdate toFDetailUpdate(){
        return new FDetailUpdate(id, detailDescription, startDateTime, endDateTime);
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