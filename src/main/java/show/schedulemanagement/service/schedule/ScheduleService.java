package show.schedulemanagement.service.schedule;

import show.schedulemanagement.domain.schedule.Schedule;

public interface ScheduleService {
    void save(Schedule Schedule);
    void deleteById(Long id);
    Schedule findById(Long id);

}
