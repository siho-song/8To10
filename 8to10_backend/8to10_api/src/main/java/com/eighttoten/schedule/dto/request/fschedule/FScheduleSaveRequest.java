package com.eighttoten.schedule.dto.request.fschedule;

import com.eighttoten.schedule.domain.fschedule.FScheduleCreateInfo;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import com.eighttoten.schedule.dto.request.DateRangeValidatable;
import com.eighttoten.schedule.validator.fielderror.Day;
import com.eighttoten.schedule.validator.fielderror.Frequency;
import com.eighttoten.schedule.validator.fielderror.PerformInDay;
import com.eighttoten.schedule.validator.fielderror.UniqueDayList;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.fielderror.ZeroTimeInfo;
import com.eighttoten.schedule.validator.objecterror.StartBeforeEnd;
import com.eighttoten.support.ValidationGroups.FieldErrorGroup;
import com.eighttoten.support.ValidationGroups.ObjectErrorGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@StartBeforeEnd(groups = ObjectErrorGroup.class)
@Builder
@AllArgsConstructor
public class FScheduleSaveRequest implements DateRangeValidatable {
    @NotBlank(groups = FieldErrorGroup.class)
    @Size(min = 1,max = 80, groups = FieldErrorGroup.class)
    private String title;

    @NotNull(groups = FieldErrorGroup.class)
    private String commonDescription;

    @ZeroTimeInfo(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime startDateTime;

    @ZeroTimeInfo(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime endDateTime;

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
        return startDateTime;
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDateTime;
    }

    public NewFSchedule toNewFSchedule(Long memberId) {
        return new NewFSchedule(
                memberId,
                title,
                commonDescription,
                startDateTime,
                endDateTime
        );
    }

    public FScheduleCreateInfo toFScheduleCreateInfo(){
        return new FScheduleCreateInfo(startTime, duration, frequency, days);
    }
}