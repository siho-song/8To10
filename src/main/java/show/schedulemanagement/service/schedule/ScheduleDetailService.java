package show.schedulemanagement.service.schedule;

import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;

public interface ScheduleDetailService {
    void deleteFdById(Long id);
    FScheduleDetail findFdById(Long id);
}
