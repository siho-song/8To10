package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
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

    public NScheduleResponse(NDetailWithParent nDetailWithParent) {
        NSchedule nSchedule = nDetailWithParent.getNSchedule();
        id = nDetailWithParent.getId();
        title = nSchedule.getTitle();
        commonDescription = nSchedule.getCommonDescription();
        startDateTime = nDetailWithParent.getScheduleStart().plusHours(nDetailWithParent.getBufferTime().getHour())
                .plusMinutes(nDetailWithParent.getBufferTime().getMinute());
        endDateTime = nDetailWithParent.getScheduleEnd();
        type = "normal";
        color = ScheduleColor.NORMAL.hexCode;
        completeStatus = nDetailWithParent.isCompleteStatus();
        parentId = nSchedule.getId();
        dailyAmount = nDetailWithParent.getDailyAmount();
        detailDescription = nDetailWithParent.getDetailDescription();
        bufferTime = nSchedule.getBufferTime();
        achievedAmount = nDetailWithParent.getAchievedAmount();
    }
}
