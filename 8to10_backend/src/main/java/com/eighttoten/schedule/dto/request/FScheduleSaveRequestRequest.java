package com.eighttoten.schedule.dto.request;

import com.eighttoten.global.ValidationGroups.FieldErrorGroup;
import com.eighttoten.global.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.schedule.validator.fielderror.Day;
import com.eighttoten.schedule.validator.fielderror.Frequency;
import com.eighttoten.schedule.validator.fielderror.PerformInDay;
import com.eighttoten.schedule.validator.fielderror.UniqueDayList;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.objecterror.StartBeforeEnd;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@StartBeforeEnd(groups = ObjectErrorGroup.class)
public class FScheduleSaveRequestRequest extends ScheduleSaveRequest implements DateRangeValidatable {
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDate startDate;

    @NotNull(groups = FieldErrorGroup.class)
    private LocalDate endDate;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalTime startTime;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @PerformInDay(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalTime duration;

    @Frequency(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private String frequency;

    @NotNull(groups = FieldErrorGroup.class)
    @UniqueDayList(groups = FieldErrorGroup.class)
    private List<@Day @NotNull String> days;

    @Override
    public LocalDateTime takeStartDateTime() {
        return startDate.atStartOfDay();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDate.atStartOfDay();
    }
}