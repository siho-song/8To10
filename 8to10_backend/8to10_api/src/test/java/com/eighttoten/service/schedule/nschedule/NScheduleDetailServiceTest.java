package com.eighttoten.service.schedule.nschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_DETAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.schedule.dto.request.NDetailUpdateRequest;
import com.eighttoten.schedule.dto.request.ProgressUpdatesRequest;
import com.eighttoten.schedule.dto.request.ProgressUpdatesRequest.ProgressUpdateRequest;
import com.eighttoten.schedule.service.nschedule.NScheduleDetailService;
import com.eighttoten.service.MemberDetailsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("일반일정 자식일정 서비스 테스트")
class NScheduleDetailServiceTest {
    @Autowired
    NScheduleDetailRepository nScheduleDetailRepository;
    
    @Autowired
    NScheduleDetailService nScheduleDetailService;

    @Autowired
    NScheduleRepository nScheduleRepository;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("일반일정 자식일정 단건 수정")
    @WithUserDetails(value = "normal@example.com")
    void update() {
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        long id = 1L;
        String detailDescription = "수정된 메모";
        NDetailUpdateRequest request = NDetailUpdateRequest.builder()
                .id(id)
                .detailDescription(detailDescription)
                .build();

        //when
        nScheduleDetailService.update(member, request.toNDetailUpdate());

        //then
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));;
        assertThat(nScheduleDetail.getDetailDescription()).isEqualTo(detailDescription);
    }

    @Test
    @DisplayName("일반일정 자식일정 단건 삭제")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void deleteById(){
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        long id = 1L;
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        //when
        nScheduleDetailService.deleteById(member, id);

        //then
        assertThatThrownBy(() -> nScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL)))
                .isInstanceOf(NotFoundEntityException.class);

        assertThat(nScheduleRepository.findById(nScheduleDetail.getNScheduleId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_SCHEDULE)).getTotalAmount())
                .isEqualTo(80);
    }

    @Test
    @DisplayName("특정 날짜 이후의 일반일정 자식일정 삭제")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void deleteByStartDateGE(){
        //given
        LocalDateTime startDate = LocalDateTime.of(2024, 5, 2, 10, 0);
        Member member = memberDetailsService.getAuthenticatedMember();
        Long parentId = 4L;

        nScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(startDate, member, parentId);
        NSchedule nSchedule = nScheduleRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_SCHEDULE));

        //TODO
        //자식일정의 수를 확인해야한다.
        assertThat(nSchedule.getTotalAmount()).isEqualTo(20);
    }

    @Test
    @DisplayName("일반 자식일정 진행 상태 수정 - 유저가 수행한 양 수정")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void updateProgress(){
        Member member = memberDetailsService.getAuthenticatedMember();
        ProgressUpdatesRequest request = ProgressUpdatesRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .progressUpdateRequests(List.of(
                        ProgressUpdateRequest.builder()
                                .scheduleDetailId(1L)
                                .achievedAmount(10)
                                .build()
                )).build();

        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());

        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(1L)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        assertThat(nScheduleDetail.isCompleteStatus()).isFalse();
        assertThat(nScheduleDetail.getAchievedAmount()).isEqualTo(10);
    }

    @Test
    @DisplayName("일반 자식일정 진행 상태 수정 - 유저가 수행한 양 다중 수정")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void updateProgress_More(){
        Member member = memberDetailsService.getAuthenticatedMember();
        ProgressUpdatesRequest request = ProgressUpdatesRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .progressUpdateRequests(List.of(
                        ProgressUpdateRequest.builder()
                                .scheduleDetailId(1L)
                                .achievedAmount(10)
                                .build()
                        , ProgressUpdateRequest.builder()
                                .scheduleDetailId(6L)
                                .achievedAmount(10)
                                .build()
                )).build();

        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());

        NScheduleDetail nScheduleDetail1 = nScheduleDetailRepository.findById(1L)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        NScheduleDetail nScheduleDetail6 = nScheduleDetailRepository.findById(6L)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        assertThat(nScheduleDetail1.isCompleteStatus()).isFalse();
        assertThat(nScheduleDetail1.getAchievedAmount()).isEqualTo(10);
        assertThat(nScheduleDetail6.isCompleteStatus()).isFalse();
        assertThat(nScheduleDetail6.getAchievedAmount()).isEqualTo(10);
    }

    @Test
    @DisplayName("일반 자식일정 진행 상태 수정 - 수행한 양과 일간 총 수행량이 같은 경우 완료상태 업데이트")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void updateProgress_List_same_amount(){
        Member member = memberDetailsService.getAuthenticatedMember();
        ProgressUpdatesRequest request = ProgressUpdatesRequest.builder()
                .date(LocalDate.of(2024,5,1))
                .progressUpdateRequests(List.of(
                        ProgressUpdateRequest.builder()
                                .scheduleDetailId(1L)
                                .achievedAmount(20)
                                .build()
                )).build();

        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());

        NScheduleDetail nScheduleDetail1 = nScheduleDetailRepository.findById(1L)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        assertThat(nScheduleDetail1.isCompleteStatus()).isTrue();
        assertThat(nScheduleDetail1.getAchievedAmount()).isEqualTo(20);
    }
}