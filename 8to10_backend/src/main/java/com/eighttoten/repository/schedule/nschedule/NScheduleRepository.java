package com.eighttoten.repository.schedule.nschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eighttoten.domain.schedule.nschedule.NSchedule;

public interface NScheduleRepository extends JpaRepository<NSchedule, Long> {
}
