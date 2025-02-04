package com.eighttoten.service.schedule.vschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import com.eighttoten.schedule.dto.request.vschedule.VScheduleUpdateRequest;
import com.eighttoten.schedule.service.vschedule.VScheduleService;
import com.eighttoten.service.MemberDetailsService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("변동일정 서비스 테스트")
class VScheduleServiceTest {
    @Autowired
    VScheduleRepository vScheduleRepository;

    @Autowired
    VScheduleService vScheduleService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("변동일정 수정 서비스 테스트")
    @WithUserDetails(value = "normal@example.com")
    void update() {
        //given
        VScheduleUpdateRequest request = new VScheduleUpdateRequest(8L, "수정된 변동일정", "수정된 변동일정 입니다.",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2L));
        Member member = memberDetailsService.getAuthenticatedMember();

        //when
        vScheduleService.update(member, request.toVScheduleUpdate());

        //then
        VSchedule vSchedule = vScheduleRepository.findById(8L).orElseThrow(() -> new NotFoundEntityException(
                ExceptionCode.NOT_FOUND_V_SCHEDULE));

        assertThat(vSchedule.getTitle()).isEqualTo(request.getTitle());
        assertThat(vSchedule.getStartDateTime()).isEqualTo(request.getStartDateTime());
    }

    @Test
    @DisplayName("변동일정 삭제")
    @WithUserDetails(value = "normal@example.com")
    @Transactional
    void deleteById(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long scheduleId = 7L;
        vScheduleService.deleteById(member, scheduleId);
        assertThatThrownBy(() -> vScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_V_SCHEDULE)))
                .isInstanceOf(NotFoundEntityException.class);
    }
}