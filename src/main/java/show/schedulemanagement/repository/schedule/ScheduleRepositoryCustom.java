package show.schedulemanagement.repository.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;

public interface ScheduleRepositoryCustom {

    List<Schedule> findAll(Member member);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDateTime start, LocalDateTime end);
}
