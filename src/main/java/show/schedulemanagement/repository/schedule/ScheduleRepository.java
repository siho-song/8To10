package show.schedulemanagement.repository.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule,Long>, ScheduleRepositoryCustom{
    //findScheduleByMemberId 모든 스케쥴 다 들고오기
    List<Schedule> findByMemberId(Long memberId);
}
