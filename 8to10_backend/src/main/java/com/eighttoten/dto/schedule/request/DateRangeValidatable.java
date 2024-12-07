package com.eighttoten.dto.schedule.request;

import java.time.LocalDateTime;

public interface DateRangeValidatable {
    LocalDateTime takeStartDateTime();
    LocalDateTime takeEndDateTime();
}