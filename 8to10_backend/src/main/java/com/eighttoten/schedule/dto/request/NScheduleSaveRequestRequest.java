package com.eighttoten.schedule.dto.request;

import com.eighttoten.global.ValidationGroups.FieldErrorGroup;
import com.eighttoten.global.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.schedule.validator.fielderror.PerformInDay;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.objecterror.PerformInWeek;
import com.eighttoten.schedule.validator.objecterror.StartDateBeforeEqEndDate;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@PerformInWeek(groups = ObjectErrorGroup.class)
@StartDateBeforeEqEndDate(groups = ObjectErrorGroup.class)
public class NScheduleSaveRequestRequest extends ScheduleSaveRequest implements DateRangeValidatable {
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDate startDate;

    @NotNull(groups = FieldErrorGroup.class)
    private LocalDate endDate;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalTime bufferTime;

    @PerformInDay(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalTime performInDay;

    @NotNull(groups = FieldErrorGroup.class)
    private Boolean isIncludeSaturday;

    @NotNull(groups = FieldErrorGroup.class)
    private Boolean isIncludeSunday;

    private int totalAmount;
    private int performInWeek;

    public List<DayOfWeek> getDays() {
        List<DayOfWeek> days = new ArrayList<>(
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY));

        if(isIncludeSaturday) {
            days.add(DayOfWeek.SATURDAY);
        }
        if (isIncludeSunday) {
            days.add(DayOfWeek.SUNDAY);
        }
        return days;
    }

    public Duration getNecessaryTime() {
        long hour = bufferTime.getHour() + performInDay.getHour();
        long minute = bufferTime.getMinute() + performInDay.getMinute();
        if(minute >= 60) {
            hour += minute / 60;
            minute = minute % 60;
        }
        return Duration.ofHours(hour).plusMinutes(minute);
    }

    @Override
    public LocalDateTime takeStartDateTime() {
        return this.getStartDate().atStartOfDay();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return this.getEndDate().atStartOfDay();
    }
}