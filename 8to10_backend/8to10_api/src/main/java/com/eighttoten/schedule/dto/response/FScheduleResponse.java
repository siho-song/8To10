package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class FScheduleResponse extends ScheduleResponse {
    private Long parentId;
    private String detailDescription;

    public FScheduleResponse(FDetailWithParent fDetailWithParent) {
        FSchedule fSchedule = fDetailWithParent.getFSchedule();
        id = fDetailWithParent.getId();
        title = fSchedule.getTitle();
        commonDescription = fSchedule.getCommonDescription();
        startDateTime = fDetailWithParent.getScheduleStart();
        endDateTime = fDetailWithParent.getScheduleEnd();
        type = "fixed";
        color = ScheduleColor.FIXED.hexCode;
        parentId = fSchedule.getId();
        detailDescription = fDetailWithParent.getDetailDescription();
    }
}