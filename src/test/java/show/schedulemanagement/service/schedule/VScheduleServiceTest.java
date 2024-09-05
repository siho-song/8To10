package show.schedulemanagement.service.schedule;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
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
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.dto.schedule.request.vSchedule.VScheduleUpdate;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.vSchedule.VScheduleService;

@SpringBootTest
@DisplayName("변동일정 서비스 테스트")
@Transactional
class VScheduleServiceTest {

    @Autowired
    VScheduleService vScheduleService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto memberDetailsDto = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetailsDto, null,
                memberDetailsDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("변동일정 수정 서비스 테스트")
    void update() {
        VScheduleUpdate vScheduleUpdate = new VScheduleUpdate();
        vScheduleUpdate.setId(7L);
        vScheduleUpdate.setTitle("수정된 변동일정");
        vScheduleUpdate.setCommonDescription("수정된 변동일정 입니다.");
        vScheduleUpdate.setStartDate(LocalDateTime.now());
        vScheduleUpdate.setEndDate(LocalDateTime.now().plusHours(2L));
        Member member = memberService.getAuthenticatedMember();

        vScheduleService.update(member, vScheduleUpdate);

        Schedule schedule = scheduleService.findById(7L);
        assertThat(schedule.getTitle()).isEqualTo(vScheduleUpdate.getTitle());
        assertThat(schedule.getStartDate()).isEqualTo(vScheduleUpdate.getStartDate());
    }
}