package com.eighttoten.dto.schedule.response.vschedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.eighttoten.domain.schedule.vschedule.VSchedule;
import com.eighttoten.dto.schedule.ScheduleColor;
import com.eighttoten.dto.schedule.response.ScheduleResponse;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
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
