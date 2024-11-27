package com.eighttoten.repository.schedule.nschedule;

import com.eighttoten.domain.schedule.nschedule.NScheduleDetail;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NScheduleDetailRepository extends JpaRepository<NScheduleDetail, Long> {
    @Modifying
    @Query("delete from NScheduleDetail nd where nd in :nScheduleDetails")
    void deleteByNScheduleDetails(@Param(value = "nScheduleDetails") List<NScheduleDetail> nScheduleDetails);

    @EntityGraph(attributePaths = "nSchedule")
    @Query("select nd from NScheduleDetail nd where nd.id = :id")
    Optional<NScheduleDetail> findByIdWithParent(@Param(value = "id") Long id);

    @Query("select nd from NScheduleDetail nd where nd.startDate >= :start and nd.nSchedule.id = :parentId and nd.createdBy = :email")
    List<NScheduleDetail> findByStartDateGEAndEmailAndParentId(@Param(value = "start") LocalDateTime start,
                                                               @Param(value = "email") String email,
                                                               @Param(value = "parentId") Long parentId);

    @Query("select  nd from NScheduleDetail nd where DATE(nd.startDate) = :date and nd.createdBy = :email")
    List<NScheduleDetail> findAllByDateAndEmail(@Param(value = "date") LocalDate date,
                                                @Param(value = "email") String email);
}
