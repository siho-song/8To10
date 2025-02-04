package com.eighttoten.schedule.domain.nschedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewNSchedule {
    private Long memberId;
    private String title;
    private String commonDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private int totalAmount;

}
