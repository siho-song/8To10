package com.eighttoten.service.schedule.vschedule;

import static org.assertj.core.api.Assertions.*;

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
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleUpdate;
import com.eighttoten.domain.auth.MemberDetails;
import com.eighttoten.service.member.MemberService;
import com.eighttoten.service.schedule.ScheduleService;

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
        VScheduleUpdate vScheduleUpdate = new VScheduleUpdate();
        vScheduleUpdate.setId(8L);
        vScheduleUpdate.setTitle("수정된 변동일정");
        vScheduleUpdate.setCommonDescription("수정된 변동일정 입니다.");
        vScheduleUpdate.setStartDate(LocalDateTime.now());
        vScheduleUpdate.setEndDate(LocalDateTime.now().plusHours(2L));
        Member member = memberService.getAuthenticatedMember();

        vScheduleService.update(member, vScheduleUpdate);

        Schedule schedule = scheduleService.findById(8L);
        assertThat(schedule.getTitle()).isEqualTo(vScheduleUpdate.getTitle());
        assertThat(schedule.getStartDate()).isEqualTo(vScheduleUpdate.getStartDate());
    }
}