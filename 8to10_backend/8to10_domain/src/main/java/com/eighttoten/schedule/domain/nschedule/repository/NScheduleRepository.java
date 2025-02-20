package com.eighttoten.schedule.domain.nschedule.repository;

import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import java.util.Optional;

public interface NScheduleRepository {
    long save(NewNSchedule newNSchedule);
    void update(NSchedule nSchedule);
    void deleteById(Long id);
    Optional<NSchedule> findById(Long id);
}