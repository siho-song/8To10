package com.eighttoten.dto.schedule.response;

import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.domain.schedule.fschedule.FSchedule;
import com.eighttoten.domain.schedule.fschedule.FScheduleDetail;
import com.eighttoten.domain.schedule.nschedule.NSchedule;
import com.eighttoten.domain.schedule.nschedule.NScheduleDetail;
import com.eighttoten.domain.schedule.vschedule.VSchedule;
import com.eighttoten.dto.schedule.response.fschedule.FScheduleResponse;
import com.eighttoten.dto.schedule.response.nschedule.NScheduleResponse;
import com.eighttoten.dto.schedule.response.vschedule.VScheduleResponse;
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