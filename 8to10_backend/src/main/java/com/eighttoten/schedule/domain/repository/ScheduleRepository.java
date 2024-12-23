package com.eighttoten.schedule.domain.repository;

import com.eighttoten.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long>, ScheduleRepositoryCustom{
}