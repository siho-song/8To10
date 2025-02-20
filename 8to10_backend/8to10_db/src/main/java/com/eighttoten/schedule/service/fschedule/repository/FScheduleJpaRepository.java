package com.eighttoten.schedule.service.fschedule.repository;

import com.eighttoten.schedule.service.fschedule.FScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FScheduleJpaRepository extends JpaRepository<FScheduleEntity, Long> {
}
