package show.schedulemanagement.dto.schedule.request;

import java.time.LocalDateTime;

public interface DateRangeValidatable {
    LocalDateTime takeStartDateTime();
    LocalDateTime takeEndDateTime();
}
