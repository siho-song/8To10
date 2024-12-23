package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.FSchedule;
import com.eighttoten.schedule.domain.FScheduleDetail;
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