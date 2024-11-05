package show.schedulemanagement.repository.schedule.nschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;

public interface NScheduleRepository extends JpaRepository<NSchedule, Long> {
}
