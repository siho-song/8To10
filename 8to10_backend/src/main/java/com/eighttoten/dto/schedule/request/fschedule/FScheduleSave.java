package com.eighttoten.dto.schedule.request.fschedule;

import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import com.eighttoten.dto.schedule.request.ScheduleSave;
import com.eighttoten.validator.ValidationGroups.FieldErrorGroup;
import com.eighttoten.validator.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.validator.schedule.fielderror.Day;
import com.eighttoten.validator.schedule.fielderror.Frequency;
import com.eighttoten.validator.schedule.fielderror.PerformInDay;
import com.eighttoten.validator.schedule.fielderror.UniqueDayList;
import com.eighttoten.validator.schedule.fielderror.ZeroSeconds;
import com.eighttoten.validator.schedule.objecterror.StartBeforeEnd;
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
public class FScheduleSave extends ScheduleSave implements DateRangeValidatable {
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