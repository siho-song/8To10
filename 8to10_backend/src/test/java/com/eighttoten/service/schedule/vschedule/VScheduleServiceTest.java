package com.eighttoten.service.schedule.vschedule;

import static org.assertj.core.api.Assertions.*;

import com.eighttoten.schedule.service.VScheduleService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.Schedule;
import com.eighttoten.schedule.dto.request.VScheduleUpdateRequest;
import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.schedule.service.ScheduleService;

@SpringBootTest
@DisplayName("변동일정 서비스 테스트")
@Transactional
class VScheduleServiceTest {

    @Autowired
    VScheduleService vScheduleService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetails memberDetails = new MemberDetails(memberService.findByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null,
                memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("변동일정 수정 서비스 테스트")
    void update() {
        VScheduleUpdateRequest vScheduleUpdateRequest = new VScheduleUpdateRequest();
        vScheduleUpdateRequest.setId(8L);
        vScheduleUpdateRequest.setTitle("수정된 변동일정");
        vScheduleUpdateRequest.setCommonDescription("수정된 변동일정 입니다.");
        vScheduleUpdateRequest.setStartDate(LocalDateTime.now());
        vScheduleUpdateRequest.setEndDate(LocalDateTime.now().plusHours(2L));
        Member member = memberService.getAuthenticatedMember();

        vScheduleService.update(member, vScheduleUpdateRequest);

        Schedule schedule = scheduleService.findById(8L);
        assertThat(schedule.getTitle()).isEqualTo(vScheduleUpdateRequest.getTitle());
        assertThat(schedule.getStartDate()).isEqualTo(vScheduleUpdateRequest.getStartDate());
    }
}