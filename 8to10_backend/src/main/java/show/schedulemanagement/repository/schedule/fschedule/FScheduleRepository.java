package show.schedulemanagement.repository.schedule.fschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.fschedule.FSchedule;

public interface FScheduleRepository extends JpaRepository<FSchedule, Long> {
}
