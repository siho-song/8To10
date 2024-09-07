package show.schedulemanagement.dto.schedule.request.nSchedule;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
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
import show.schedulemanagement.validator.schedule.filedError.PerformInDay;
import show.schedulemanagement.validator.schedule.filedError.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objectError.PerformInWeek;
import show.schedulemanagement.validator.schedule.objectError.StartDateBeforeEqEndDate;

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
    private LocalTime bufferTime;
    @PerformInDay
    @ZeroSeconds
    private LocalTime performInDay;
    @NotNull
    private Boolean isIncludeSaturday;
    @NotNull
    private Boolean isIncludeSunday;
    private int totalAmount;
    private int performInWeek;

    public List<DayOfWeek> getCandidateDays() {
        List<DayOfWeek> candidateDays = new ArrayList<>(
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY));

        if(isIncludeSaturday) {
            candidateDays.add(DayOfWeek.SATURDAY);
        }
        if (isIncludeSunday) {
            candidateDays.add(DayOfWeek.SUNDAY);
        }
        return candidateDays;
    }

    public LocalTime getNecessaryTime() {
        return bufferTime.plusHours(performInDay.getHour()).plusMinutes(performInDay.getMinute());
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
