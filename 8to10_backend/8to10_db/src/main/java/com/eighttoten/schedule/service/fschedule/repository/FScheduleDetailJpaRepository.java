package com.eighttoten.schedule.service.fschedule.repository;

import com.eighttoten.schedule.service.fschedule.FScheduleDetailEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FScheduleDetailJpaRepository extends JpaRepository<FScheduleDetailEntity, Long> {

    @Modifying
    @Query("delete from FScheduleDetailEntity fd where fd.id in :ids")
    void deleteAllByIds(@Param(value = "ids") List<Long> ids);

    @Query("select fd from FScheduleDetailEntity fd where fd.startDateTime >= :start and fd.createdBy = :email and fd.fScheduleEntity.id = :parentId")
    List<FScheduleDetailEntity> findAllByStartDateGEAndEmailAndParentId(@Param(value = "start") LocalDateTime start,
                                                                        @Param(value = "email") String email,
                                                                        @Param(value = "parentId") Long parentId);

    @Query("select fd from FScheduleDetailEntity fd where fd.startDateTime >= :start and fd.endDateTime <= :end and fd.createdBy = :memberEmail")
    List<FScheduleDetailEntity> findAllBetweenStartAndEnd(@Param(value = "memberEmail") String memberEmail,
                                                          @Param(value = "start") LocalDateTime start,
                                                          @Param(value = "end") LocalDateTime end);

    @EntityGraph(attributePaths = "fScheduleEntity")
    @Query("select fd from FScheduleDetailEntity fd where fd.createdBy = :memberEmail")
    List<FScheduleDetailEntity> findAllWithParentByMemberEmail(@Param(value = "memberEmail") String memberEmail);
}