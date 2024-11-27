package com.eighttoten.repository.schedule.nschedule;

import com.eighttoten.domain.schedule.nschedule.NSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NScheduleRepository extends JpaRepository<NSchedule, Long> {
}