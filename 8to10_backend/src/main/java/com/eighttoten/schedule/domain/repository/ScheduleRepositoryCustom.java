package com.eighttoten.schedule.domain.repository;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.Schedule;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> findAllWithDetailByMember(Member member);
    List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDateTime start, LocalDateTime end);
}