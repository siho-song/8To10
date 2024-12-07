package com.eighttoten.repository.schedule.fschedule;

import com.eighttoten.domain.schedule.fschedule.FSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FScheduleRepository extends JpaRepository<FSchedule, Long> {
}