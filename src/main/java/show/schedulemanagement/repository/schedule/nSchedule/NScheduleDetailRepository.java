package show.schedulemanagement.repository.schedule.nschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;

public interface NScheduleDetailRepository extends JpaRepository<NScheduleDetail, Long> {
}
