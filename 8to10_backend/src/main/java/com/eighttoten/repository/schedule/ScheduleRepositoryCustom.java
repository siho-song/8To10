package com.eighttoten.repository.schedule;

import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> findAllWithDetailByMember(Member member);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDateTime start, LocalDateTime end);
}