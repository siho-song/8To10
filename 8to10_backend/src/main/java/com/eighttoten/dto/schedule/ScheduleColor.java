package com.eighttoten.dto.schedule;

public enum ScheduleColor {
    VARIABLE("#f4511e"), NORMAL("#4CAF50"), FIXED("#3788d8");

    public final String hexCode;

    ScheduleColor(String hexCode) {
        this.hexCode = hexCode;
    }
}