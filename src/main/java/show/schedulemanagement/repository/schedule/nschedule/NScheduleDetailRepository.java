package show.schedulemanagement.repository.schedule.nschedule;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;

public interface NScheduleDetailRepository extends JpaRepository<NScheduleDetail, Long> {
    @EntityGraph(attributePaths = "nSchedule")
    @Query("select nd from NScheduleDetail nd where nd.id = :id")
    Optional<NScheduleDetail> findByIdWithParent(@Param(value = "id") Long id);
}
