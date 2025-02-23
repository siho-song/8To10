package com.eighttoten.schedule.domain.fschedule.repository;

import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import java.util.Optional;

public interface FScheduleRepository{
    void deleteById(Long id);
    void update(FSchedule fSchedule);
    long save(NewFSchedule newFSchedule);
    Optional<FSchedule> findById(Long id);
}