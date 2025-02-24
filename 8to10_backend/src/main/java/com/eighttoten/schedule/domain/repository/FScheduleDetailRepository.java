package com.eighttoten.schedule.domain.repository;

import com.eighttoten.schedule.domain.FScheduleDetail;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FScheduleDetailRepository extends JpaRepository<FScheduleDetail, Long> {
    @Query("select fd from FScheduleDetail fd where fd.startDate >= :start and fd.createdBy = :email and fd.fSchedule.id = :parentId")
    List<FScheduleDetail> findByStartDateGEAndEmailAndParentId(@Param(value = "start") LocalDateTime start,
                                                               @Param(value = "email") String email,
                                                               @Param(value = "parentId") Long parentId);

    @Modifying
    @Query("delete from FScheduleDetail fd where fd in :fScheduleDetails")
    void deleteByFScheduleDetails(@Param(value = "fScheduleDetails") List<FScheduleDetail> fScheduleDetails);
}