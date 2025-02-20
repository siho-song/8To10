package com.eighttoten.schedule.domain.nschedule.repository;

import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NewNDetail;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NScheduleDetailRepository {
    void deleteById(Long id);
    void deleteAllByIds(List<Long> ids);
    void update(NScheduleDetail nScheduleDetail);
    void saveAll(Long nScheduleId, List<NewNDetail> newNDetails);

    Optional<NScheduleDetail> findById(Long id);
    Optional<NDetailWithParent> findByIdWithParent(Long id);

    List<NScheduleDetail> findAllByIds(List<Long> ids);
    List<NScheduleDetail> findAllByEmailAndDate(String email, LocalDate date);
    List<NScheduleDetail> findAllByEmailBetweenStartAndEnd(String email, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<NScheduleDetail> findAllByEmailAndParentIdGEStartDate(String email, Long parentId, LocalDateTime start);

    List<NDetailWithParent> findAllWithParentByMemberEmail(String email);
}