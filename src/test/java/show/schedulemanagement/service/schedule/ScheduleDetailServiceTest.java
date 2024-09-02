package show.schedulemanagement.service.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.util.List;
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
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DisplayName("자식일정 서비스 테스트")
class ScheduleDetailServiceTest {
    @Autowired
    ScheduleDetailService scheduleDetailService;

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
    @DisplayName(value = "고정일정의 자식일정을 삭제한다. 만약 고정일정의 자식의 크기가 0 이라면 해당 고정일정도 삭제한다.")
    @Transactional
    void deleteFdById() {
        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = (FSchedule) scheduleService.findById(1L);
        List<FScheduleDetail> fScheduleDetails = fSchedule.getFScheduleDetails();
        assertThat(fScheduleDetails.size()).isEqualTo(6);

        List<Long> ids = fScheduleDetails.stream().map(FScheduleDetail::getId).toList();
        for (Long id : ids) {
            scheduleDetailService.deleteFdById(member, id);
        }
        entityManager.flush();
        entityManager.clear();
        assertThatThrownBy(() -> scheduleService.findById(1L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName(value = "일반일정의 자식일정을 삭제한다. 만약 일반일정의 자식의 크기가 0 이라면 해당 고정일정도 삭제한다.")
    @Transactional
    void deleteNdById() {
        Member member = memberService.getAuthenticatedMember();
        NSchedule nSchedule = (NSchedule) scheduleService.findById(4L);
        List<NScheduleDetail> nScheduleDetails = nSchedule.getNScheduleDetails();
        assertThat(nScheduleDetails.size()).isEqualTo(5);

        List<Long> ids = nScheduleDetails.stream().map(NScheduleDetail::getId).toList();
        for (Long id : ids) {
            scheduleDetailService.deleteNdById(member, id);
        }
        entityManager.flush();
        entityManager.clear();
        assertThatThrownBy(() -> scheduleService.findById(4L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName(value = "일반일정의 자식일정을 삭제하면 일반일정의 총량이 조정된다.")
    @Transactional
    void deleteNd_minusUpdate_TotalAmount(){
        Member member = memberService.getAuthenticatedMember();
        NSchedule nSchedule = (NSchedule) scheduleService.findById(4L);
        Integer totalAmount = nSchedule.getTotalAmount();

        assertThat(totalAmount).isEqualTo(100);
        scheduleDetailService.deleteNdById(member, 1L);
        entityManager.flush();
        entityManager.clear();
        assertThat(nSchedule.getTotalAmount()).isEqualTo(80);
    }
}