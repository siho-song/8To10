package show.schedulemanagement.service.schedule.nschedule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.security.utils.TokenUtils;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("일반일정 자식일정 서비스 테스트")
class NScheduleDetailServiceTest {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    NScheduleDetailService nScheduleDetailService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto member = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 수정")
    void update() {
        Member member = memberService.getAuthenticatedMember();
        long id = 1L;
        String detailDescription = "수정된 메모";
        NScheduleDetailUpdate dto = NScheduleDetailUpdate.builder()
                .id(id)
                .detailDescription(detailDescription)
                .build();

        nScheduleDetailService.update(member, dto);
        NScheduleDetail nScheduleDetail = nScheduleDetailService.findById(id);
        assertThat(nScheduleDetail.getDetailDescription()).isEqualTo(detailDescription);
    }
}