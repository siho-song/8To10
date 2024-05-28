package show.schedulemanagement.dto.schedule;

public enum ScheduleColor {
    VARIABLE("#f4511e"), NORMAL("#4CAF50"), FIXED("#3788d8");

    final String hexCode;

    ScheduleColor(String hexCode) {
        this.hexCode = hexCode;
    }
}
