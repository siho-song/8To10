package show.schedulemanagement.dto.schedule.request.nschedule;

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
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.dto.schedule.request.ScheduleSave;
import show.schedulemanagement.validator.schedule.fielderror.PerformInDay;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.PerformInWeek;
import show.schedulemanagement.validator.schedule.objecterror.StartDateBeforeEqEndDate;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@Slf4j
@PerformInWeek
@StartDateBeforeEqEndDate
public class NScheduleSave extends ScheduleSave implements DateRangeValidatable {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @ZeroSeconds
    @NotNull
    private LocalTime bufferTime;
    @PerformInDay
    @ZeroSeconds
    @NotNull
    private LocalTime performInDay;
    @NotNull
    private Boolean isIncludeSaturday;
    @NotNull
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
