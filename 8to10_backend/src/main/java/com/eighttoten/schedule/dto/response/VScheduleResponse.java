package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.VSchedule;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class VScheduleResponse extends ScheduleResponse {
    public VScheduleResponse(VSchedule schedule) {
        super(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getCommonDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                "variable",
                ScheduleColor.VARIABLE.hexCode
        );
    }
}