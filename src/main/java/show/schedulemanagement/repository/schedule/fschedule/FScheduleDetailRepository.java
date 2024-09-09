package show.schedulemanagement.repository.schedule.fschedule;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.schedule.fschedule.FSchedule;
import show.schedulemanagement.domain.schedule.fschedule.FScheduleDetail;

public interface FScheduleDetailRepository extends JpaRepository<FScheduleDetail, Long> {
    @Query("select fd from FScheduleDetail fd where fd.startDate >= :start and fd.createdBy = :email and fd.fSchedule = :fSchedule")
    List<FScheduleDetail> findByStartDateGEAndEmailAndParent(@Param(value = "start") LocalDateTime start,
                                                             @Param(value = "email") String email,
                                                             @Param(value = "fSchedule") FSchedule fSchedule);

    @Modifying
    @Query("delete from FScheduleDetail fd where fd in :fScheduleDetails")
    int deleteByFScheduleDetails(@Param(value = "fScheduleDetails") List<FScheduleDetail> fScheduleDetails);
}
