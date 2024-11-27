package com.eighttoten.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eighttoten.domain.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule,Long>, ScheduleRepositoryCustom{
}
