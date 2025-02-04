package com.eighttoten.service.schedule.nschedule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import com.eighttoten.schedule.dto.request.NScheduleSaveRequest;
import com.eighttoten.schedule.dto.request.NScheduleUpdateRequest;
import com.eighttoten.schedule.service.nschedule.NScheduleService;
import com.eighttoten.service.MemberDetailsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("일반일정 서비스 테스트")
@Transactional
class NScheduleServiceTest {
    @Autowired
    NScheduleRepository nScheduleRepository;

    @Autowired
    NScheduleDetailRepository nScheduleDetailRepository;

    @Autowired
    NScheduleService nScheduleService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("일반일정 정상 생성")
    @WithUserDetails(value = "normal@example.com")
    void create(){
        Member member = memberDetailsService.getAuthenticatedMember();
        String title = "테스트용 일반일정";
        String commonDescription = "테스트용 일반일정 메모";
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2020, 1, 1).plusMonths(1), LocalTime.of(0, 0));

        NScheduleSaveRequest request = NScheduleSaveRequest.builder()
                .title(title)
                .commonDescription(commonDescription)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .bufferTime(LocalTime.of(2, 0))
                .performInDay(LocalTime.of(3,0))
                .isIncludeSaturday(true)
                .isIncludeSunday(true)
                .totalAmount(200)
                .performInWeek(7)
                .build();

        nScheduleService.saveWithDetails(member, request.toNScheduleCreateInfo(), request.toNewNSchedule(member.getId()));

//TODO
//        nScheduleRepository.fin
//        assertThat(nScheduleEntity.getTitle()).isEqualTo(title);
//        assertThat(nScheduleEntity.getCommonDescription()).isEqualTo(commonDescription);
//        assertThat(nScheduleEntity.getNScheduleDetailEntities().size()).isEqualTo(32);
    }

    @Test
    @DisplayName("일반일정 생성 - 일과시간보다 더 많은 수행시간으로 생성시 예외발생")
    @WithUserDetails(value = "normal@example.com")
    void create_exception(){
        Member member = memberDetailsService.getAuthenticatedMember();
        String title = "테스트용 일반일정";
        String commonDescription = "테스트용 일반일정 메모";
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now().plusMonths(1), LocalTime.of(0, 0));
        NScheduleSaveRequest request = NScheduleSaveRequest.builder()
                .title(title)
                .commonDescription(commonDescription)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .bufferTime(LocalTime.of(2, 0))
                .performInDay(LocalTime.of(22,0))
                .isIncludeSaturday(true)
                .isIncludeSunday(true)
                .totalAmount(200)
                .performInWeek(5)
                .build();

        assertThatThrownBy(() -> nScheduleService.saveWithDetails(member, request.toNScheduleCreateInfo(),
                request.toNewNSchedule(member.getId()))).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("일반일정 수정 정상 작동")
    @WithUserDetails(value = "normal@example.com")
    void update(){

        Member member = memberDetailsService.getAuthenticatedMember();
        NScheduleUpdateRequest request = NScheduleUpdateRequest.builder()
                .id(4L)
                .title("수정된 일정")
                .commonDescription("수정된 일정 메모")
                .build();

        nScheduleService.update(member, request.toNScheduleUpdate());
    }
}