package com.eighttoten.schedule.domain.repository;

import com.eighttoten.schedule.domain.NSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NScheduleRepository extends JpaRepository<NSchedule, Long> {
}