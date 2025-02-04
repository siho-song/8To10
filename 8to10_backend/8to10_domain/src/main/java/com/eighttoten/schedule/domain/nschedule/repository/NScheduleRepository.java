package com.eighttoten.schedule.domain.nschedule.repository;

import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NewNSchedule;
import java.util.Optional;

public interface NScheduleRepository {
    void deleteById(Long id);
    void update(NSchedule nSchedule);
    long save(NewNSchedule newNSchedule);
    Optional<NSchedule> findById(Long id);
}