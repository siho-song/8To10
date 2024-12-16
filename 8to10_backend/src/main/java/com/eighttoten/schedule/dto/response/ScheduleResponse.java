package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.FSchedule;
import com.eighttoten.schedule.domain.FScheduleDetail;
import com.eighttoten.schedule.domain.NSchedule;
import com.eighttoten.schedule.domain.NScheduleDetail;
import com.eighttoten.schedule.domain.Schedule;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.VSchedule;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ScheduleResponse {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime start;
    private LocalDateTime end;
    private String type; // "normal","fixed","variable"
    private String color;

    public static ScheduleResponse from(Schedule schedule, ScheduleAble scheduleAble) {
        if (schedule instanceof NSchedule) {
            return new NScheduleResponse((NSchedule) schedule, (NScheduleDetail) scheduleAble);
        } else if (schedule instanceof FSchedule) {
            return new FScheduleResponse((FSchedule) schedule,(FScheduleDetail) scheduleAble);
        } else if (schedule instanceof VSchedule) {
            return new VScheduleResponse((VSchedule) schedule);
        } else {
            throw new IllegalArgumentException("Unknown schedule type");
        }
    }
}