package show.schedulemanagement.repository.schedule;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.repository.member.MemberRepository;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@Transactional
class ScheduleRepositoryCustomImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ScheduleRepository scheduleRepository;


    @Test
    void findAllBetweenStartAndEnd() {
        Member member = memberService.findByEmail("user1@example.com");
        System.out.println("findAllBetweenStartAndEnd findByEmail 실행");

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