package com.eighttoten.service.schedule.nschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.NSchedule;
import com.eighttoten.schedule.domain.NScheduleDetail;
import com.eighttoten.schedule.dto.request.NormalDetailUpdateRequest;
import com.eighttoten.schedule.dto.request.ProgressUpdateRequest;
import com.eighttoten.schedule.dto.request.ProgressUpdateRequest.ProgressUpdate;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.infrastructure.TokenProvider;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.schedule.service.NScheduleDetailService;
import com.eighttoten.schedule.service.NScheduleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@SpringBootTest
@DisplayName("일반일정 자식일정 서비스 테스트")
@Transactional
class NScheduleDetailServiceTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    NScheduleDetailService nScheduleDetailService;

    @Autowired
    NScheduleService nScheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetails member = new MemberDetails(memberService.findByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 수정")
    void update() {
        Member member = memberService.getAuthenticatedMember();
        long id = 1L;
        String detailDescription = "수정된 메모";
        NormalDetailUpdateRequest dto = NormalDetailUpdateRequest.builder()
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

        assertThatThrownBy(() -> nScheduleDetailService.findById(id)).isInstanceOf(NotFoundEntityException.class);
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
    @DisplayName("일반 자식일정 진행 상태 수정 - 유저가 수행한 양 수정")
    void updateProgress(){
        Member member = memberService.getAuthenticatedMember();
        ProgressUpdateRequest progressUpdateRequest = ProgressUpdateRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .progressUpdates(List.of(
                        ProgressUpdate.builder()
                                .scheduleDetailId(1L)
                                .achievedAmount(10)
                                .build()
                )).build();

        nScheduleDetailService.updateProgressList(member,progressUpdateRequest);

        assertThat(nScheduleDetailService.findById(1L).isCompleteStatus()).isFalse();
        assertThat(nScheduleDetailService.findById(1L).getAchievedAmount()).isEqualTo(10);
    }

    @Test
    @DisplayName("일반 자식일정 진행 상태 수정 - 유저가 수행한 양 다중 수정")
    void updateProgress_More(){
        Member member = memberService.getAuthenticatedMember();
        ProgressUpdateRequest progressUpdateRequest = ProgressUpdateRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .progressUpdates(List.of(
                        ProgressUpdate.builder()
                                .scheduleDetailId(1L)
                                .achievedAmount(10)
                                .build()
                        ,ProgressUpdate.builder()
                                .scheduleDetailId(6L)
                                .achievedAmount(10)
                                .build()
                )).build();

        nScheduleDetailService.updateProgressList(member,progressUpdateRequest);

        assertThat(nScheduleDetailService.findById(1L).isCompleteStatus()).isFalse();
        assertThat(nScheduleDetailService.findById(1L).getAchievedAmount()).isEqualTo(10);
        assertThat(nScheduleDetailService.findById(6L).isCompleteStatus()).isFalse();
        assertThat(nScheduleDetailService.findById(6L).getAchievedAmount()).isEqualTo(10);
    }

    @Test
    @DisplayName("일반 자식일정 진행 상태 수정 - 수행한 양과 일간 총 수행량이 같은 경우 완료상태 업데이트")
    void updateProgress_List_same_amount(){
        Member member = memberService.getAuthenticatedMember();
        ProgressUpdateRequest progressUpdateRequest = ProgressUpdateRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .progressUpdates(List.of(
                        ProgressUpdate.builder()
                                .scheduleDetailId(1L)
                                .achievedAmount(20)
                                .build()
                )).build();

        nScheduleDetailService.updateProgressList(member,progressUpdateRequest);

        assertThat(nScheduleDetailService.findById(1L).isCompleteStatus()).isTrue();
        assertThat(nScheduleDetailService.findById(1L).getAchievedAmount()).isEqualTo(20);
    }
}