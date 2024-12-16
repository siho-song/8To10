package com.eighttoten.schedule.domain;

import java.time.LocalDateTime;

public interface ScheduleAble {
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}