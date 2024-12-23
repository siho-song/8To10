package com.eighttoten.service.schedule.fschedule;

import static org.assertj.core.api.Assertions.assertThat;

import com.eighttoten.schedule.service.FScheduleService;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.FSchedule;
import com.eighttoten.schedule.dto.request.FScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.FScheduleUpdateRequest;
import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.infrastructure.TokenProvider;
import com.eighttoten.member.service.MemberService;

@DisplayName("고정일정 서비스 테스트")
@SpringBootTest
@Transactional
class FScheduleServiceTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    FScheduleService fScheduleService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetails member = new MemberDetails(memberService.findByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("고정일정 생성 - 자식 고정일정을 생성한다.")
    void addDetailsForEachEvent() {
        FScheduleSaveRequestRequest fScheduleSaveRequest = FScheduleSaveRequestRequest.builder()
                .title("테스트 title ")
                .commonDescription("테스트 commonDescription")
                .startDate(LocalDate.of(2024,9,3))
                .endDate(LocalDate.of(2024,9,22))
                .days(new ArrayList<>(List.of("mo", "we", "fr")))
                .frequency("weekly")
                .startTime(LocalTime.of(8,0))
                .duration(LocalTime.of(2, 0))
                .build();

        Member member = memberService.getAuthenticatedMember();
        FSchedule fSchedule = FSchedule.from(member, fScheduleSaveRequest);

        fScheduleService.addDetails(fSchedule, fScheduleSaveRequest);
        List<ScheduleAble> scheduleAbles = fSchedule.getScheduleAbles();
        assertThat(scheduleAbles.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("부모 고정일정 수정")
    void update(){
        Long id = 1L;
        String title = "고정일정 제목 수정";
        String commonDescription = "고정일정 메모 수정";
        FScheduleUpdateRequest fScheduleUpdateRequest = FScheduleUpdateRequest.builder()
                .id(id)
                .title(title)
                .commonDescription(commonDescription)
                .build();

        Member member = memberService.getAuthenticatedMember();

        fScheduleService.update(member, fScheduleUpdateRequest);

        FSchedule fSchedule = fScheduleService.findById(id);

        assertThat(fSchedule.getTitle()).isEqualTo(title);
        assertThat(fSchedule.getCommonDescription()).isEqualTo(commonDescription);
    }
}