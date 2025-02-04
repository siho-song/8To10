package com.eighttoten.schedule.domain.nschedule;

import com.eighttoten.schedule.domain.ScheduleAble;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NScheduleDetail implements ScheduleAble {
    private Long id;
    private Long nScheduleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private String detailDescription;
    private String createdBy;
    private boolean completeStatus;
    @Setter
    private int dailyAmount;
    private int achievedAmount;

    public static NScheduleDetail from(Long nScheduleId, LocalDateTime startDateTime,
                                       LocalDateTime endDateTime, String detailDescription, LocalTime bufferTime) {
        NScheduleDetail nScheduleDetail = new NScheduleDetail();
        nScheduleDetail.nScheduleId = nScheduleId;
        nScheduleDetail.startDateTime = startDateTime;
        nScheduleDetail.endDateTime = endDateTime;
        nScheduleDetail.detailDescription = detailDescription;
        nScheduleDetail.completeStatus = false;
        nScheduleDetail.bufferTime = bufferTime;
        return nScheduleDetail;
    }

    public void updateCompleteStatus(boolean completeStatus){
        this.completeStatus = completeStatus;
    }

    public void updateAchievedAmount(int achievedAmount){
        this.achievedAmount = achievedAmount;
    }

    public void updateDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public double getAchievementRate(){
        if(completeStatus){
            return 1;
        }
        if(dailyAmount == 0){
            return 0;
        }
        return (double) achievedAmount / dailyAmount;
    }

    @Override
    public LocalDateTime getScheduleStart() {
        return this.startDateTime.minusHours(bufferTime.getHour()).minusMinutes(bufferTime.getMinute());
    }

    @Override
    public LocalDateTime getScheduleEnd() {
        return endDateTime;
    }
}
