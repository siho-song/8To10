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
    void deleteByIds(List<Long> ids);
    void saveAll(Long nScheduleId, List<NewNDetail> newNDetails);
    void update(NScheduleDetail nScheduleDetail);
    Optional<NScheduleDetail> findById(Long id);
    Optional<NDetailWithParent> findByIdWithParent(Long id);
    List<NScheduleDetail> findByStartDateGEAndEmailAndParentId(LocalDateTime start, String email, Long parentId);
    List<NScheduleDetail> findAllByEmailAndDate(String email, LocalDate date);
    List<NScheduleDetail> findAllByIds(List<Long> ids);
    List<NScheduleDetail> findAllBetweenStartAndEnd(String memberEmail, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<NDetailWithParent> findAllWithParentByMemberEmail(String memberEmail);
}