package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.dto.schedule.request.fSchedule.FScheduleDetailSave;
import show.schedulemanagement.dto.schedule.request.fSchedule.FScheduleSave;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.security.utils.TokenUtils;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.fSchedule.FScheduleService;

@DisplayName("고정일정 서비스 테스트")
@SpringBootTest
@Transactional
class FScheduleServiceTest {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    FScheduleService fScheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto member = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("고정일정 생성 - 고정일정 생성 요청의 각 이벤트별로 다른 자식 고정일정을 생성한다.")
    void addDetailsForEachEvent() {
        List<FScheduleDetailSave> events = new ArrayList<>();
        events.add(getFixDetailAddDto(LocalTime.now(), LocalTime.of(2, 0), "weekly",
                new ArrayList<>(List.of("mo", "we", "fr"))));
        events.add(getFixDetailAddDto(LocalTime.now().plusHours(3), LocalTime.of(2, 0), "daily",
                new ArrayList<>(List.of("mo","tu", "we","th","fr","sa","su"))));

        FScheduleSave fScheduleSave = FScheduleSave.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.of(2024,9,3))
                .endDate(LocalDate.of(2024,9,22))
                .events(events)
                .build();

        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = FSchedule.createFSchedule(member, fScheduleSave);

        fScheduleService.addDetailsForEachEvent(fSchedule, fScheduleSave.getEvents());
        List<ScheduleAble> scheduleAbles = fSchedule.getScheduleAbles();
        for (ScheduleAble scheduleAble : scheduleAbles) {
            System.out.println("scheduleAble = " + scheduleAble);
        }
        Assertions.assertThat(scheduleAbles.size()).isEqualTo(28);
    }

    private FScheduleDetailSave getFixDetailAddDto(LocalTime startTime, LocalTime duration, String frequency, List<String> days) {
        return FScheduleDetailSave.builder()
                .startTime(startTime)
                .duration(duration)
                .frequency(frequency)
                .days(days)
                .build();
    }
}