package com.eighttoten.dto.schedule.response.nschedule;

import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.eighttoten.domain.schedule.nschedule.NSchedule;
import com.eighttoten.domain.schedule.nschedule.NScheduleDetail;
import com.eighttoten.dto.schedule.ScheduleColor;
import com.eighttoten.dto.schedule.response.ScheduleResponse;

@SuperBuilder
@Getter
public class NScheduleResponse extends ScheduleResponse {
    private Long parentId;
    private String detailDescription;
    private int dailyAmount;
    private LocalTime bufferTime;
    private boolean completeStatus;

    public NScheduleResponse(NSchedule nSchedule, NScheduleDetail nScheduleDetail) {
        super(nScheduleDetail.getId(),
                nSchedule.getTitle(),
                nSchedule.getCommonDescription(),
                nScheduleDetail.getStartDate().plusHours(nSchedule.getBufferTime().getHour())
                        .plusMinutes(nSchedule.getBufferTime().getMinute()),
                nScheduleDetail.getEndDate(),
                "normal",
                ScheduleColor.NORMAL.hexCode);

        this.completeStatus = nScheduleDetail.isCompleteStatus();
        this.parentId = nSchedule.getId();
        this.dailyAmount = nScheduleDetail.getDailyAmount();
        this.detailDescription = nSchedule.getCommonDescription();
        this.bufferTime = nSchedule.getBufferTime();
    }
}
