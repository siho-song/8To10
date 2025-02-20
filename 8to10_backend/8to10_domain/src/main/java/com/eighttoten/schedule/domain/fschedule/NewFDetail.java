package com.eighttoten.schedule.domain.fschedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewFDetail {
    private Long fScheduleId;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
