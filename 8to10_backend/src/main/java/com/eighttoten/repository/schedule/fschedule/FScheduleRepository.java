package com.eighttoten.repository.schedule.fschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eighttoten.domain.schedule.fschedule.FSchedule;

public interface FScheduleRepository extends JpaRepository<FSchedule, Long> {
}
