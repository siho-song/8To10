package com.eighttoten.schedule.dto.request.vschedule;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.dto.request.DateRangeValidatable;
import com.eighttoten.schedule.validator.fielderror.ZeroSeconds;
import com.eighttoten.schedule.validator.objecterror.StartBeforeEnd;
import com.eighttoten.support.ValidationGroups.FieldErrorGroup;
import com.eighttoten.support.ValidationGroups.ObjectErrorGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@StartBeforeEnd(groups = ObjectErrorGroup.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VScheduleSaveRequest implements DateRangeValidatable {
    @NotBlank(groups = FieldErrorGroup.class)
    @Size(min = 1,max = 80, groups = FieldErrorGroup.class)
    private String title;

    @NotNull
    private String commonDescription;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime startDateTime;

    @ZeroSeconds(groups = FieldErrorGroup.class)
    @NotNull(groups = FieldErrorGroup.class)
    private LocalDateTime endDateTime;

    public NewVSchedule toNewVSchedule(Long memberId){
        return new NewVSchedule(memberId, title, commonDescription,
                startDateTime, endDateTime);
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