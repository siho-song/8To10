package com.eighttoten.service.schedule.fschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleRepository;
import com.eighttoten.schedule.dto.request.fschedule.FScheduleSaveRequest;
import com.eighttoten.schedule.dto.request.fschedule.FScheduleUpdateRequest;
import com.eighttoten.schedule.service.fschedule.FScheduleService;
import com.eighttoten.service.MemberDetailsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("고정일정 서비스 테스트")
@SpringBootTest
class FScheduleServiceTest {
    @Autowired
    FScheduleService fScheduleService;

    @Autowired
    FScheduleRepository fScheduleRepository;

    @Autowired
    FScheduleDetailRepository fScheduleDetailRepository;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("고정일정 생성 - 자식 고정일정을 생성한다.")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void addDetailsForEachEvent() {
        //given
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2024, 9, 3), LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2024, 9, 22), LocalTime.of(0, 0));

        FScheduleSaveRequest request = FScheduleSaveRequest.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .days(new ArrayList<>(List.of("mo", "we", "fr")))
                .frequency("weekly")
                .startTime(LocalTime.of(8,0))
                .duration(LocalTime.of(2, 0))
                .build();

        Member member = memberDetailsService.getAuthenticatedMember();

        //when
        fScheduleService.save(request.toNewFSchedule(member.getId()));

        //then
        List<FScheduleDetail> fScheduleDetails = fScheduleDetailRepository.findAllBetweenStartAndEnd(
                member.getEmail(), startDateTime, endDateTime);

        assertThat(fScheduleDetails.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("고정일정 수정")
    @WithUserDetails(value = "normal@example.com")
    void update(){
        //given
        Long id = 1L;
        String title = "고정일정 제목 수정";
        String commonDescription = "고정일정 메모 수정";
        FScheduleUpdateRequest request = FScheduleUpdateRequest.builder()
                .id(id)
                .title(title)
                .commonDescription(commonDescription)
                .build();

        Member member = memberDetailsService.getAuthenticatedMember();

        //when
        fScheduleService.update(member, request.toFScheduleUpdate());

        FSchedule after = fScheduleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(
                ExceptionCode.NOT_FOUND_F_SCHEDULE));

        //then
        assertThat(after.getTitle()).isEqualTo(title);
        assertThat(after.getCommonDescription()).isEqualTo(commonDescription);
    }

    @Test
    @DisplayName("고정일정 삭제 - 자식일정까지 함께 삭제")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void delete_FSchedule() {
        Member member = memberDetailsService.getAuthenticatedMember();
        Long scheduleId = 1L;
        fScheduleService.deleteById(member, scheduleId);

        assertThatThrownBy(() -> fScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_SCHEDULE)))
                .isInstanceOf(NotFoundEntityException.class);

        //TODO 자식일정들에 대한 검증이 필요하다.
    }
}