package com.eighttoten.schedule.domain.nschedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class NewNDetail {
    private Long nScheduleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private String detailDescription;
    private boolean completeStatus;
    @Setter
    private int dailyAmount;
    private int achievedAmount;
}
