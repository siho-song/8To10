package show.schedulemanagement.domain.schedule;

import java.time.LocalDateTime;

public interface ScheduleAble {
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}
