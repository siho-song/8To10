package com.eighttoten.schedule.domain.fschedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewFSchedule {
    private Long memberId;
    private String title;
    private String commonDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
