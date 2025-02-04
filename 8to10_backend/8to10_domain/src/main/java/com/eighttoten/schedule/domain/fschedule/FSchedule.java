package com.eighttoten.schedule.domain.fschedule;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class FSchedule {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String createdBy;

    public FSchedule(Long id, String title, String commonDescription,
                     LocalDateTime startDateTime, LocalDateTime endDateTime, String createdBy) {
        this.id = id;
        this.title = title;
        this.commonDescription = commonDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdBy = createdBy;
    }

    public void update(String title, String commonDescription){
        this.title = title;
        this.commonDescription = commonDescription;
    }
}
