package com.eighttoten.domain.schedule;

import java.time.LocalDateTime;

public interface ScheduleAble {
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}
