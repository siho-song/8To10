package show.schedulemanagement.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule,Long>, ScheduleRepositoryCustom{
}
