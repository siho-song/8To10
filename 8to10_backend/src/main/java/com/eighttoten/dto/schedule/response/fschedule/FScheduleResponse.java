package com.eighttoten.dto.schedule.response.fschedule;

import com.eighttoten.domain.schedule.fschedule.FSchedule;
import com.eighttoten.domain.schedule.fschedule.FScheduleDetail;
import com.eighttoten.dto.schedule.ScheduleColor;
import com.eighttoten.dto.schedule.response.ScheduleResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class FScheduleResponse extends ScheduleResponse {
    private Long parentId;
    private String detailDescription;
    public FScheduleResponse(FSchedule fSchedule, FScheduleDetail fScheduleDetail) {
        super(fScheduleDetail.getId(),
                fSchedule.getTitle(),
                fSchedule.getCommonDescription(),
                fScheduleDetail.getStartDate(),
                fScheduleDetail.getEndDate(),
                "fixed",
                ScheduleColor.FIXED.hexCode);

        this.parentId = fSchedule.getId();
        this.detailDescription = fScheduleDetail.getDetailDescription();
    }
}