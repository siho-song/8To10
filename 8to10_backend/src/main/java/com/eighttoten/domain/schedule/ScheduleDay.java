package com.eighttoten.domain.schedule;

import java.time.DayOfWeek;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ScheduleDay {
    MONDAY("mo",DayOfWeek.MONDAY),
    TUESDAY("tu",DayOfWeek.TUESDAY),
    WEDNESDAY("we",DayOfWeek.WEDNESDAY),
    THURSDAY("th",DayOfWeek.THURSDAY),
    FRIDAY("fr",DayOfWeek.FRIDAY),
    SATURDAY("sa",DayOfWeek.SATURDAY),
    SUNDAY("su",DayOfWeek.SUNDAY);

    final String day;
    final DayOfWeek value;

    ScheduleDay(String day, DayOfWeek value) {
        this.day = day;
        this.value = value;
    }

    public static DayOfWeek of(String day){
        return Arrays.stream(ScheduleDay.values()).filter(value -> value.day.equals(day))
                .map(ScheduleDay::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 day(String) 입니다."));
    }

    public static String from(DayOfWeek day){
        return Arrays.stream(ScheduleDay.values()).filter(value -> value.value.equals(day))
                .map(ScheduleDay::getDay)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 day(DayOfWeek) 입니다."));
    }
}
