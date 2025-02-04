package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.vschedule.VSchedule;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class VScheduleResponse extends ScheduleResponse {
    public VScheduleResponse(VSchedule vSchedule) {
        id = vSchedule.getId();
        title = vSchedule.getTitle();
        commonDescription = vSchedule.getCommonDescription();
        startDateTime = vSchedule.getScheduleStart();
        endDateTime = vSchedule.getScheduleEnd();
        type = "variable";
        color = ScheduleColor.VARIABLE.hexCode;
    }
}