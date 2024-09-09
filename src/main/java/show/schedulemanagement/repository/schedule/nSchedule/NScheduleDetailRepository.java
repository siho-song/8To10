package show.schedulemanagement.repository.schedule.nschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;

public interface NScheduleDetailRepository extends JpaRepository<NScheduleDetail, Long> {
}
