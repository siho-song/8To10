package com.eighttoten.repository.schedule;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.service.member.MemberService;

@SpringBootTest
@Transactional
class ScheduleRepositoryCustomImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ScheduleRepository scheduleRepository;


    @Test
    void findAllBetweenStartAndEnd() { //TODO 테스트 수정
        Member member = memberService.findByEmail("normal@example.com");

        List<Schedule> allBetweenStartAndEnd = scheduleRepository
                .findAllBetweenStartAndEnd(
                        member,
                        LocalDateTime.of(2024,Month.JANUARY,1,0,0),
                        LocalDateTime.of(2024, Month.SEPTEMBER, 1,0,0));

        for (Schedule schedule : allBetweenStartAndEnd) {
            System.out.println("schedule = " + schedule);
        }

    }
}