package com.eighttoten.schedule.domain.fschedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewFScheduleDetail {
    private Long fScheduleId;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public static NewFScheduleDetail from(Long fScheduleId,
                                          String detailDescription,
                                          LocalDateTime startDateTime,
                                          LocalDateTime endDateTime)
    {
        return new NewFScheduleDetail(fScheduleId, detailDescription, startDateTime, endDateTime);
    }
}
