package com.eighttoten.schedule.domain.nschedule;

import static com.eighttoten.common.AppConstant.WORK_END_TIME;
import static com.eighttoten.common.AppConstant.WORK_START_TIME;

import java.time.LocalTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean isWithinWorkTime(){
        return startTime.isAfter(WORK_START_TIME.minusSeconds(1)) && endTime.isBefore(WORK_END_TIME.plusSeconds(1));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) obj;

        return Objects.equals(startTime, timeSlot.startTime) &&
                Objects.equals(endTime, timeSlot.endTime);
    }

    @Override
    public int hashCode(){
        return Objects.hash(startTime, endTime);
    }
}