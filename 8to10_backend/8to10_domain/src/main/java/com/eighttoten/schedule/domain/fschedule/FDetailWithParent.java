package com.eighttoten.schedule.domain.fschedule;

import com.eighttoten.schedule.domain.ScheduleAble;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FDetailWithParent implements ScheduleAble {
    private Long id;
    private FSchedule fSchedule;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String createdBy;

    @Override
    public LocalDateTime getScheduleStart() {
        return startDateTime;
    }

    @Override
    public LocalDateTime getScheduleEnd() { return endDateTime; }
}
