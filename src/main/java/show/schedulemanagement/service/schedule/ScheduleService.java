package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.util.List;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponseDto;

public interface ScheduleService {
    void save(Schedule Schedule);
    void deleteById(Member member, Long id);
    Schedule findById(Long id);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);
    List<Schedule> findAllByMemberAndDetail(Member member);
    void setResultFromSchedule(Result<ScheduleResponseDto> result, Schedule schedule);
}
