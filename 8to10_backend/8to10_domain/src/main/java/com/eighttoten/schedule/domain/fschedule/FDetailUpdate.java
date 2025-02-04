package com.eighttoten.schedule.domain.fschedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FDetailUpdate {
    private Long id;
    private String detailDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
