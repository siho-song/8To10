package com.eighttoten.schedule.service.nschedule;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("일반일정 통합테스트")
@SpringBootTest
public class NScheduleIntegrationTest {
//    @Test
//    @DisplayName("일반일정 정상 생성")
//    @WithUserDetails(value = "normal@example.com")
//    void create(){
//        Member member = memberDetailsService.getAuthenticatedMember();
//        String title = "테스트용 일반일정";
//        String commonDescription = "테스트용 일반일정 메모";
//        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0, 0));
//        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2020, 1, 1).plusMonths(1), LocalTime.of(0, 0));
//
//        NScheduleSaveRequest request = NScheduleSaveRequest.builder()
//                .title(title)
//                .commonDescription(commonDescription)
//                .startDateTime(startDateTime)
//                .endDateTime(endDateTime)
//                .bufferTime(LocalTime.of(2, 0))
//                .performInDay(LocalTime.of(3,0))
//                .isIncludeSaturday(true)
//                .isIncludeSunday(true)
//                .totalAmount(200)
//                .performInWeek(7)
//                .build();
//
//        nScheduleService.saveWithDetails(member, request.toNScheduleCreateInfo(), request.toNewNSchedule(member.getId()));
//
////TODO
////        nScheduleRepository.fin
////        assertThat(nScheduleEntity.getTitle()).isEqualTo(title);
////        assertThat(nScheduleEntity.getCommonDescription()).isEqualTo(commonDescription);
////        assertThat(nScheduleEntity.getNScheduleDetailEntities().size()).isEqualTo(32);
//    }
//
//    @Test
//    @DisplayName("일반일정 생성 - 일과시간보다 더 많은 수행시간으로 생성시 예외발생")
//    @WithUserDetails(value = "normal@example.com")
//    void create_exception(){
//        Member member = memberDetailsService.getAuthenticatedMember();
//        String title = "테스트용 일반일정";
//        String commonDescription = "테스트용 일반일정 메모";
//        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
//        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now().plusMonths(1), LocalTime.of(0, 0));
//        NScheduleSaveRequest request = NScheduleSaveRequest.builder()
//                .title(title)
//                .commonDescription(commonDescription)
//                .startDateTime(startDateTime)
//                .endDateTime(endDateTime)
//                .bufferTime(LocalTime.of(2, 0))
//                .performInDay(LocalTime.of(22,0))
//                .isIncludeSaturday(true)
//                .isIncludeSunday(true)
//                .totalAmount(200)
//                .performInWeek(5)
//                .build();
//
//        assertThatThrownBy(() -> nScheduleService.saveWithDetails(member, request.toNScheduleCreateInfo(),
//                request.toNewNSchedule(member.getId()))).isInstanceOf(RuntimeException.class);
//    }

//    @Test
//    @DisplayName("일반일정 자식일정 단건 삭제")
//    @WithUserDetails(value = "normal@example.com")
//    @Transactional
//    void deleteById(){
//        //given
//        Member member = memberDetailsService.getAuthenticatedMember();
//        long id = 1L;
//        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(id)
//                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
//
//        //when
//        nScheduleDetailService.deleteById(member, id);
//
//        //then
//        assertThatThrownBy(() -> nScheduleDetailRepository.findById(id)
//                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL)))
//                .isInstanceOf(NotFoundEntityException.class);
//
//        assertThat(nScheduleRepository.findById(nScheduleDetail.getNScheduleId())
//                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_SCHEDULE)).getTotalAmount())
//                .isEqualTo(80);
//    }
//
//    @Test
//    @DisplayName("특정 날짜 이후의 일반일정 자식일정 삭제")
//    @WithUserDetails(value = "normal@example.com")
//    @Transactional
//    void deleteByStartDateGE(){
//        //given
//        LocalDateTime startDate = LocalDateTime.of(2024, 5, 2, 10, 0);
//        Member member = memberDetailsService.getAuthenticatedMember();
//        Long parentId = 4L;
//
//        nScheduleDetailService.deleteByStartDateGEAndMemberAndParentId(startDate, member, parentId);
//        NSchedule nSchedule = nScheduleRepository.findById(parentId)
//                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_SCHEDULE));
//
//        //TODO
//        //자식일정의 수를 확인해야한다.
//        assertThat(nSchedule.getTotalAmount()).isEqualTo(20);
//    }
//
//    @Test
//    @DisplayName("일반 자식일정 진행 상태 수정 - 유저가 수행한 양 수정")
//    @WithUserDetails(value = "normal@example.com")
//    @Transactional
//    void updateProgress(){
//        Member member = memberDetailsService.getAuthenticatedMember();
//        ProgressUpdatesRequest request = ProgressUpdatesRequest.builder()
//                .date(LocalDate.of(2024,5,1))
//                .progressUpdateRequests(List.of(
//                        ProgressUpdateRequest.builder()
//                                .scheduleDetailId(1L)
//                                .achievedAmount(10)
//                                .build()
//                )).build();
//
//        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());
//
//        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(1L)
//                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
//
//        assertThat(nScheduleDetail.isCompleteStatus()).isFalse();
//        assertThat(nScheduleDetail.getAchievedAmount()).isEqualTo(10);
//    }
//
//    @Test
//    @DisplayName("일반 자식일정 진행 상태 수정 - 유저가 수행한 양 다중 수정")
//    @WithUserDetails(value = "normal@example.com")
//    @Transactional
//    void updateProgress_More(){
//        Member member = memberDetailsService.getAuthenticatedMember();
//        ProgressUpdatesRequest request = ProgressUpdatesRequest.builder()
//                .date(LocalDate.of(2024,5,1))
//                .progressUpdateRequests(List.of(
//                        ProgressUpdateRequest.builder()
//                                .scheduleDetailId(1L)
//                                .achievedAmount(10)
//                                .build()
//                        , ProgressUpdateRequest.builder()
//                                .scheduleDetailId(6L)
//                                .achievedAmount(10)
//                                .build()
//                )).build();
//
//        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());
//
//        NScheduleDetail nScheduleDetail1 = nScheduleDetailRepository.findById(1L)
//                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
//
//        NScheduleDetail nScheduleDetail6 = nScheduleDetailRepository.findById(6L)
//                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
//
//        assertThat(nScheduleDetail1.isCompleteStatus()).isFalse();
//        assertThat(nScheduleDetail1.getAchievedAmount()).isEqualTo(10);
//        assertThat(nScheduleDetail6.isCompleteStatus()).isFalse();
//        assertThat(nScheduleDetail6.getAchievedAmount()).isEqualTo(10);
//    }
//
//    @Test
//    @DisplayName("일반 자식일정 진행 상태 수정 - 수행한 양과 일간 총 수행량이 같은 경우 완료상태 업데이트")
//    @WithUserDetails(value = "normal@example.com")
//    @Transactional
//    void updateProgress_List_same_amount(){
//        Member member = memberDetailsService.getAuthenticatedMember();
//        ProgressUpdatesRequest request = ProgressUpdatesRequest.builder()
//                .date(LocalDate.of(2024,5,1))
//                .progressUpdateRequests(List.of(
//                        ProgressUpdateRequest.builder()
//                                .scheduleDetailId(1L)
//                                .achievedAmount(20)
//                                .build()
//                )).build();
//
//        nScheduleDetailService.updateProgressList(member, request.toProgressUpdates());
//
//        NScheduleDetail nScheduleDetail1 = nScheduleDetailRepository.findById(1L)
//                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
//
//        assertThat(nScheduleDetail1.isCompleteStatus()).isTrue();
//        assertThat(nScheduleDetail1.getAchievedAmount()).isEqualTo(20);
//    }
}
