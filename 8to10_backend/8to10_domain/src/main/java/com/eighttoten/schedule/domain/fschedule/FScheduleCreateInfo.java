package com.eighttoten.schedule.domain.fschedule;

import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

@Getter
public class FScheduleCreateInfo {
    private final LocalTime startTime;
    private final LocalTime duration;
    private final String frequency;
    private final List<String> days;

    public FScheduleCreateInfo(LocalTime startTime, LocalTime duration, String frequency, List<String> days) {
        this.startTime = startTime;
        this.duration = duration;
        this.frequency = frequency;
        this.days = days;
    }
}
