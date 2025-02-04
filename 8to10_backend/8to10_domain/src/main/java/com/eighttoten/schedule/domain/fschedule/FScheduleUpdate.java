package com.eighttoten.schedule.domain.fschedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FScheduleUpdate {
    private Long id;
    private String title;
    private String commonDescription;
}
