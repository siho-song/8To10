package show.schedulemanagement.service.schedule.fSchedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
@DisplayName("고정일정 자식일정 서비스 테스트")
class FScheduleDetailServiceTest {

    @Autowired
    FScheduleDetailService fScheduleDetailService;

    @Autowired
    FScheduleService fScheduleService;

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
    void deleteById() {
        Member member = memberService.getAuthenticatedMember();
        Long id = 1L;
        fScheduleDetailService.deleteById(member,id);

        assertThatThrownBy(() -> fScheduleDetailService.findById(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName(value = "특정날짜 이후의 자식일정 모두 삭제 정상작동")
    void deleteByStartDateGE(){
        Member member = memberService.getAuthenticatedMember();
        Long parentId = 1L;
        FSchedule parent = fScheduleService.findById(parentId);
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 15, 30);

        int deletedCount = fScheduleDetailService.deleteByStartDateGEAndMemberAndParent(start, member, parent);
        assertThat(deletedCount).isEqualTo(5L);
    }
}