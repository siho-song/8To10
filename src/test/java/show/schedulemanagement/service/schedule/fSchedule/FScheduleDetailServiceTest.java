package show.schedulemanagement.service.schedule.fSchedule;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.schedule.ScheduleService;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
@DisplayName("고정일정 자식일정 서비스 테스트")
class FScheduleDetailServiceTest {

    @Autowired
    FScheduleDetailService fScheduleDetailService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setAuthentication(){
        MemberDetailsDto user = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName(value = "고정일정의 자식일정 단건삭제 정상작동")
    @Transactional
    void deleteById() {
        Member member = memberService.getAuthenticatedMember();
        Long id = 1L;
        fScheduleDetailService.deleteById(member,id);

        assertThatThrownBy(() -> fScheduleDetailService.findById(1L)).isInstanceOf(EntityNotFoundException.class);
    }
}