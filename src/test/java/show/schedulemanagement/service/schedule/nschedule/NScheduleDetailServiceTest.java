package show.schedulemanagement.service.schedule.nschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.ToDoUpdate;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.security.utils.TokenUtils;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("일반일정 자식일정 서비스 테스트")
@Transactional
class NScheduleDetailServiceTest {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    NScheduleDetailService nScheduleDetailService;

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

    @Test
    @DisplayName("일반일정 자식일정 단건 삭제")
    void deleteById(){
        Member member = memberService.getAuthenticatedMember();
        long id = 1L;
        NScheduleDetail nScheduleDetail = nScheduleDetailService.findByIdWithParent(id);
        NSchedule parent = nScheduleDetail.getNSchedule();
        nScheduleDetailService.deleteById(member, id);

        assertThatThrownBy(() -> nScheduleDetailService.findById(id)).isInstanceOf(EntityNotFoundException.class);
        assertThat(parent.getTotalAmount()).isEqualTo(80);
    }

    @Test
    @DisplayName("특정 날짜 이후의 일반일정 자식일정 삭제")
    void deleteByStartDateGE(){
        LocalDateTime startDate = LocalDateTime.of(2024, 5, 2, 10, 0);
        Member member = memberService.getAuthenticatedMember();
        Long parentId = 4L;

        nScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(startDate, member, parentId);
        NSchedule nSchedule = nScheduleService.findById(parentId);

        assertThat(nSchedule.getNScheduleDetails().size()).isEqualTo(1);
        assertThat(nSchedule.getTotalAmount()).isEqualTo(20);
    }

    @Test
    @DisplayName("일반 자식일정 ToDo 상태 수정")
    void updateToDoList(){
        Member member = memberService.getAuthenticatedMember();
        List<ToDoUpdate> toDoUpdates = createToDoUpdates(true,1L, 2L, 3L);

        nScheduleDetailService.updateCompleteStatuses(member, toDoUpdates);

        assertThat(nScheduleDetailService.findById(1L).isCompleteStatus()).isTrue();
        assertThat(nScheduleDetailService.findById(2L).isCompleteStatus()).isTrue();
        assertThat(nScheduleDetailService.findById(3L).isCompleteStatus()).isTrue();
    }

    private List<ToDoUpdate> createToDoUpdates(boolean completeStatus, Long... ids) {
        List<ToDoUpdate> toDoUpdates = new ArrayList<>();
        for (Long id : ids) {
            toDoUpdates.add(ToDoUpdate.builder()
                    .id(id)
                    .isComplete(completeStatus)
                    .build());
        }
        return toDoUpdates;
    }
}