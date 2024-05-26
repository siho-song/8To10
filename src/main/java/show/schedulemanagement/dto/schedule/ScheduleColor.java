package show.schedulemanagement.dto.schedule;

public enum ScheduleColor {
    RED("#f4511e"),GREEN("#4CAF50"),BLUE("#3788d8");

    final String hexCode;

    ScheduleColor(String hexCode) {
        this.hexCode = hexCode;
    }
}
