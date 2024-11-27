package com.eighttoten.dto.schedule.response.fschedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.eighttoten.domain.schedule.fschedule.FSchedule;
import com.eighttoten.domain.schedule.fschedule.FScheduleDetail;
import com.eighttoten.dto.schedule.ScheduleColor;
import com.eighttoten.dto.schedule.response.ScheduleResponse;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class FScheduleResponse extends ScheduleResponse {
    private Long parentId;
    public FScheduleResponse(FSchedule fSchedule, FScheduleDetail fScheduleDetail) {
        super(fScheduleDetail.getId(),
                fSchedule.getTitle(),
                fSchedule.getCommonDescription(),
                fScheduleDetail.getStartDate(),
                fScheduleDetail.getEndDate(),
                "fixed",
                ScheduleColor.FIXED.hexCode);

        this.parentId = fSchedule.getId();
    }
}
