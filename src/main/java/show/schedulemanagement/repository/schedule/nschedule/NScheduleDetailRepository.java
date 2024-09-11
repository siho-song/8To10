package show.schedulemanagement.repository.schedule.nschedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;

public interface NScheduleDetailRepository extends JpaRepository<NScheduleDetail, Long> {
    @EntityGraph(attributePaths = "nSchedule")
    @Query("select nd from NScheduleDetail nd where nd.id = :id")
    Optional<NScheduleDetail> findByIdWithParent(@Param(value = "id") Long id);

    @Query("select nd from NScheduleDetail nd where nd.startDate >= :start and nd.nSchedule.id = :parentId and nd.createdBy = :email")
    List<NScheduleDetail> findByStartDateGEAndEmailAndParentId(@Param(value = "start") LocalDateTime start,
                                                               @Param(value = "email") String email,
                                                               @Param(value = "parentId") Long parentId);

    @Modifying
    @Query("delete from NScheduleDetail nd where nd in :nScheduleDetails")
    void deleteByNScheduleDetails(@Param(value = "nScheduleDetails") List<NScheduleDetail> nScheduleDetails);
}
