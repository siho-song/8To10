package com.eighttoten.schedule.service.nschedule.repository;

import com.eighttoten.schedule.service.nschedule.NScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NScheduleJpaRepository extends JpaRepository<NScheduleEntity, Long> {

}
