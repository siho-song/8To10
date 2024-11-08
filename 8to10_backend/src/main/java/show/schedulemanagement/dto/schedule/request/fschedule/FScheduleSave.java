package show.schedulemanagement.dto.schedule.request.fschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.dto.schedule.request.ScheduleSave;
import show.schedulemanagement.validator.ValidationGroups.FieldErrorGroup;
import show.schedulemanagement.validator.ValidationGroups.ObjectErrorGroup;
import show.schedulemanagement.validator.schedule.fielderror.Day;
import show.schedulemanagement.validator.schedule.fielderror.Frequency;
import show.schedulemanagement.validator.schedule.fielderror.PerformInDay;
import show.schedulemanagement.validator.schedule.fielderror.UniqueDayList;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@SuperBuilder
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
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