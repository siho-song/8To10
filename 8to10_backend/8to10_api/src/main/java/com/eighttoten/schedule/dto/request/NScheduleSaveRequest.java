package com.eighttoten.schedule.dto.request;

import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import com.eighttoten.schedule.validator.fielderror.ZeroTimeInfo;
import com.eighttoten.support.ValidationGroups.FieldErrorGroup;
import com.eighttoten.support.ValidationGroups.ObjectErrorGroup;
import com.eighttoten.schedule.domain.nschedule.NScheduleCreateInfo;
import com.eighttoten.schedule.validator.fielderror.PerformInDay;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.objecterror.PerformInWeek;
import com.eighttoten.schedule.validator.objecterror.StartDateBeforeEqEndDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PerformInWeek(groups = ObjectErrorGroup.class)
@StartDateBeforeEqEndDate(groups = ObjectErrorGroup.class)
public class NScheduleSaveRequest implements DateRangeValidatable {
    @NotBlank(groups = FieldErrorGroup.class)
    @Size(min = 1,max = 80, groups = FieldErrorGroup.class)
    private String title;

    @NotNull(groups = FieldErrorGroup.class)
    private String commonDescription;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroTimeInfo(groups = FieldErrorGroup.class)
    private LocalDateTime startDateTime;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroTimeInfo(groups = FieldErrorGroup.class)
    private LocalDateTime endDateTime;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    private LocalTime bufferTime;

    @NotNull(groups = FieldErrorGroup.class)
    @ZeroSeconds(groups = FieldErrorGroup.class)
    @PerformInDay(groups = FieldErrorGroup.class)
    private LocalTime performInDay;

    @NotNull(groups = FieldErrorGroup.class)
    private Boolean isIncludeSaturday;

    @NotNull(groups = FieldErrorGroup.class)
    private Boolean isIncludeSunday;

    private int totalAmount;
    private int performInWeek;

    public NScheduleCreateInfo toNScheduleCreateInfo(){
        return new NScheduleCreateInfo(
                bufferTime,
                performInDay,
                isIncludeSaturday,
                isIncludeSunday,
                performInWeek
        );
    }

    public NewNSchedule toNewNSchedule(Long memberId){
        return new NewNSchedule(memberId, title, commonDescription, startDateTime, endDateTime, bufferTime,
                totalAmount);
    }

    @Override
    public LocalDateTime takeStartDateTime() {
        return getStartDateTime();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return getEndDateTime();
    }
}