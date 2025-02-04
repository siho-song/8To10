package com.eighttoten.schedule.domain.nschedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NScheduleUpdate {
    private Long id;
    private String title;
    private String commonDescription;
}
