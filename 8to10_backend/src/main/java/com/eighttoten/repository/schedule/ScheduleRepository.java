package com.eighttoten.repository.schedule;

import com.eighttoten.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long>, ScheduleRepositoryCustom{
}