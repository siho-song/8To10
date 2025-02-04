package com.eighttoten.schedule.domain.vschedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VScheduleUpdate {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}