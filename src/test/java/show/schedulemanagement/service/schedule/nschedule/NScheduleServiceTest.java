package show.schedulemanagement.service.schedule.nschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
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
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleSave;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleUpdate;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.security.utils.TokenUtils;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("일반일정 서비스 테스트")
@Transactional
class NScheduleServiceTest {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    NScheduleService nScheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto member = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("일반일정 정상 생성")
    void create(){
        Member member = memberService.getAuthenticatedMember();
        String title = "테스트용 일반일정";
        String commonDescription = "테스트용 일반일정 메모";
        NScheduleSave nScheduleSave = NScheduleSave.builder()
                .title(title)
                .commonDescription(commonDescription)
                .startDate(LocalDate.of(2020,1,1))
                .endDate(LocalDate.of(2020,1,1).plusMonths(1L))
                .bufferTime(LocalTime.of(2, 0))
                .performInDay(LocalTime.of(3,0))
                .isIncludeSaturday(true)
                .isIncludeSunday(true)
                .totalAmount(200)
                .performInWeek(7)
                .build();

        NSchedule nSchedule = nScheduleService.create(member, nScheduleSave);

        assertThat(nSchedule.getTitle()).isEqualTo(title);
        assertThat(nSchedule.getCommonDescription()).isEqualTo(commonDescription);
        assertThat(nSchedule.getNScheduleDetails().size()).isEqualTo(32);
    }

    @Test
    @DisplayName("일반일정 생성 예외발생")
    void create_exception(){
        Member member = memberService.getAuthenticatedMember();
        String title = "테스트용 일반일정";
        String commonDescription = "테스트용 일반일정 메모";
        NScheduleSave nScheduleSave = NScheduleSave.builder()
                .title(title)
                .commonDescription(commonDescription)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1L))
                .bufferTime(LocalTime.of(2, 0))
                .performInDay(LocalTime.of(22,0))
                .isIncludeSaturday(true)
                .isIncludeSunday(true)
                .totalAmount(200)
                .performInWeek(5)
                .build();

        assertThatThrownBy(() -> nScheduleService.create(member, nScheduleSave)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("일반일정 수정 정상 작동")
    void update(){

        Member member = memberService.getAuthenticatedMember();
        NScheduleUpdate nScheduleUpdate = NScheduleUpdate.builder()
                .id(4L)
                .title("수정된 일정")
                .commonDescription("수정된 일정 메모")
                .build();

        nScheduleService.update(member,nScheduleUpdate);
    }
}