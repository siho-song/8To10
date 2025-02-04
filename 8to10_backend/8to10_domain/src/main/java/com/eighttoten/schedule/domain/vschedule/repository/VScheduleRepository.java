package com.eighttoten.schedule.domain.vschedule.repository;

import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.VScheduleUpdate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VScheduleRepository {
    void save(NewVSchedule newVSchedule);
    void update(VScheduleUpdate vScheduleUpdate);
    void deleteById(Long id);
    Optional<VSchedule> findById(Long id);
    List<VSchedule> findAllBetweenStartAndEnd(String memberEmail, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<VSchedule> findAllByMemberEmail(String memberEmail);
}