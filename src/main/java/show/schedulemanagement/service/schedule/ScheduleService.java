package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.util.List;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.dto.schedule.response.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;

public interface ScheduleService {
    void save(Schedule Schedule);
    void deleteById(Long id) throws Exception;
    Schedule findById(Long id);
    Schedule getConflictSchedule(List<ScheduleAble> newSchedule, List<Schedule> allSchedule);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);
    List<Schedule> findAll(Member member);
    Result<ScheduleResponseDto> getResult(Member member);
}
