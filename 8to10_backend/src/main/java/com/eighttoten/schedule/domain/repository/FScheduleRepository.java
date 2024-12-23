package com.eighttoten.schedule.domain.repository;

import com.eighttoten.schedule.domain.FSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FScheduleRepository extends JpaRepository<FSchedule, Long> {
}