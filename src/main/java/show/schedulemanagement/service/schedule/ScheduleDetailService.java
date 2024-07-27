package show.schedulemanagement.service.schedule;

import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;

public interface ScheduleDetailService {
    void deleteFdById(Long id);
    FScheduleDetail findFdById(Long id);
    NScheduleDetail findNdById(Long id);
}
