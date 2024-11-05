package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.util.List;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;

public interface ScheduleService {
    void save(Schedule Schedule);
    void deleteById(Member member, Long id);
    Schedule findById(Long id);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);
    List<Schedule> findAllWithDetailByMember(Member member);
    List<ScheduleAble> getAllScheduleAbles(List<Schedule> schedules);
}
