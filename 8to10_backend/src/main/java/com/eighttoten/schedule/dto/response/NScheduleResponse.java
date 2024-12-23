package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.NSchedule;
import com.eighttoten.schedule.domain.NScheduleDetail;
import java.time.LocalTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class NScheduleResponse extends ScheduleResponse {
    private Long parentId;
    private String detailDescription;
    private int dailyAmount;
    private int achievedAmount;
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
        this.detailDescription = nScheduleDetail.getDetailDescription();
        this.bufferTime = nSchedule.getBufferTime();
        this.achievedAmount = nScheduleDetail.getAchievedAmount();
    }
}
