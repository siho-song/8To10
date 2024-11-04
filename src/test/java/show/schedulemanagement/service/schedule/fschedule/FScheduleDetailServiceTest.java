package show.schedulemanagement.service.schedule.fschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
import show.schedulemanagement.domain.schedule.fschedule.FSchedule;
import show.schedulemanagement.domain.schedule.fschedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleDetailUpdate;
import show.schedulemanagement.domain.auth.MemberDetails;
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
        MemberDetails user = new MemberDetails(memberService.findByEmail("normal@example.com"));
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
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 15, 30);

        fScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(start, member, parentId);
        FSchedule fSchedule = fScheduleService.findById(1L);
        List<FScheduleDetail> fScheduleDetails = fSchedule.getFScheduleDetails();
        assertThat(fScheduleDetails.size()).isEqualTo(1L);
    }

    @Test
    @DisplayName(value = "자식일정 단건 업데이트")
    void update(){
        Member member = memberService.getAuthenticatedMember();
        Long id = 1L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 9, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2024, 9, 9, 12, 30);
        String detailDescription = "수정된 메모";
        FScheduleDetailUpdate fScheduleDetailUpdate = FScheduleDetailUpdate.builder()
                .id(id)
                .detailDescription(detailDescription)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        fScheduleDetailService.update(member,fScheduleDetailUpdate);

        FScheduleDetail fScheduleDetail = fScheduleDetailService.findById(id);

        assertThat(fScheduleDetail.getDetailDescription()).isEqualTo(detailDescription);
        assertThat(fScheduleDetail.getStartDate()).isEqualTo(startDate);
        assertThat(fScheduleDetail.getEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName(value = "자식일정 단건 업데이트 예외발생- 작성자가 불일치")
    void update_not_equal_createdBy(){
        Member member = memberService.getAuthenticatedMember();
        Long id = 7L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 9, 10, 30);
        LocalDateTime endDate = LocalDateTime.of(2024, 9, 9, 12, 30);
        String detailDescription = "수정된 메모";
        FScheduleDetailUpdate fScheduleDetailUpdate = FScheduleDetailUpdate.builder()
                .id(id)
                .detailDescription(detailDescription)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        assertThatThrownBy(() -> fScheduleDetailService.update(member, fScheduleDetailUpdate)).isInstanceOf(
                RuntimeException.class);
    }
}