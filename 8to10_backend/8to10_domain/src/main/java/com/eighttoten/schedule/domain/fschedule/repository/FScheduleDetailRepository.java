package com.eighttoten.schedule.domain.fschedule.repository;

import com.eighttoten.schedule.domain.fschedule.FDetailUpdate;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFScheduleDetail;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FScheduleDetailRepository {
    void save(NewFScheduleDetail newFScheduleDetail);
    void update(FDetailUpdate fDetailUpdate);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Optional<FScheduleDetail> findById(Long id);
    List<FScheduleDetail> findAllBetweenStartAndEnd(String memberEmail, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<FScheduleDetail> findByStartDateGEAndEmailAndParentId(LocalDateTime start, String email, Long parentId);
    List<FScheduleDetail> findAllWithParentByMemberEmail(String memberEmail);
}