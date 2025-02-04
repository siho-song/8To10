package com.eighttoten.schedule.dto.request;

import java.time.LocalDateTime;

public interface DateRangeValidatable {
    LocalDateTime takeStartDateTime();
    LocalDateTime takeEndDateTime();
}