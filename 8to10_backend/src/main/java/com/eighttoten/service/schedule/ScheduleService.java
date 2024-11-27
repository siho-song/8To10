package com.eighttoten.service.schedule;

import java.time.LocalDate;
import java.util.List;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;

public interface ScheduleService {
    void save(Schedule Schedule);
    void deleteById(Member member, Long id);
    Schedule findById(Long id);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);
    List<Schedule> findAllWithDetailByMember(Member member);
    List<ScheduleAble> getAllScheduleAbles(List<Schedule> schedules);
}
