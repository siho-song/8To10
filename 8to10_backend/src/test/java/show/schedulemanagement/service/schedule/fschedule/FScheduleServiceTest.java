package show.schedulemanagement.service.schedule.fschedule;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
import show.schedulemanagement.domain.schedule.fschedule.FSchedule;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleSave;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleUpdate;
import show.schedulemanagement.domain.auth.MemberDetails;
import show.schedulemanagement.provider.TokenProvider;
import show.schedulemanagement.service.MemberService;

@DisplayName("고정일정 서비스 테스트")
@SpringBootTest
@Transactional
class FScheduleServiceTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    FScheduleService fScheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetails member = new MemberDetails(memberService.findByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("고정일정 생성 - 자식 고정일정을 생성한다.")
    void addDetailsForEachEvent() {
        FScheduleSave fScheduleSave = FScheduleSave.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.of(2024,9,3))
                .endDate(LocalDate.of(2024,9,22))
                .days(new ArrayList<>(List.of("mo", "we", "fr")))
                .frequency("weekly")
                .startTime(LocalTime.of(8,0))
                .duration(LocalTime.of(2, 0))
                .build();

        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = FSchedule.createFSchedule(member, fScheduleSave);

        fScheduleService.addDetails(fSchedule, fScheduleSave);
        List<ScheduleAble> scheduleAbles = fSchedule.getScheduleAbles();
        assertThat(scheduleAbles.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("부모 고정일정 수정")
    void update(){
        Long id = 1L;
        String title = "고정일정 제목 수정";
        String commonDescription = "고정일정 메모 수정";
        FScheduleUpdate fScheduleUpdate = FScheduleUpdate.builder()
                .id(id)
                .title(title)
                .commonDescription(commonDescription)
                .build();

        Member member = memberService.getAuthenticatedMember();

        fScheduleService.update(member,fScheduleUpdate);

        FSchedule fSchedule = fScheduleService.findById(id);

        assertThat(fSchedule.getTitle()).isEqualTo(title);
        assertThat(fSchedule.getCommonDescription()).isEqualTo(commonDescription);
    }
}