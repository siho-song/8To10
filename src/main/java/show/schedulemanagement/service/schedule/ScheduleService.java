package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.util.List;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;

public interface ScheduleService {
    void save(Schedule Schedule);
    void deleteById(Long id) throws Exception;
    Schedule findById(Long id);
    Schedule getConflictSchedule(List<ScheduleAble> newSchedule, List<Schedule> allSchedule);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);
}
