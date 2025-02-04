package com.eighttoten.schedule.domain.nschedule;

import com.eighttoten.schedule.domain.ScheduleAble;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class NDetailWithParent implements ScheduleAble {
    private Long id;
    private NSchedule nSchedule;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private String detailDescription;
    private String createdBy;
    private boolean completeStatus;
    @Setter
    private int dailyAmount;
    private int achievedAmount;

    @Override
    public LocalDateTime getScheduleStart() {
        return this.startDateTime.minusHours(bufferTime.getHour()).minusMinutes(bufferTime.getMinute());
    }

    @Override
    public LocalDateTime getScheduleEnd() { return endDateTime; }
}
