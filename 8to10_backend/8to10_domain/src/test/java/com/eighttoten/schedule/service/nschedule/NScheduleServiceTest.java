package com.eighttoten.schedule.service.nschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleUpdate;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("일반일정 서비스 테스트")
@Transactional
class NScheduleServiceTest {
    @MockBean
    NScheduleRepository nScheduleRepository;

    @Autowired
    NScheduleService nScheduleService;

    @Test
    @DisplayName("일반일정 수정에 성공한다")
    void update(){
        //given
        NScheduleUpdate nScheduleUpdate = new NScheduleUpdate(1L, "수정된 제목", "수정된 내용");
        Member member = TestDataUtils.createTestMember(1L,"normal@example.com");
        NSchedule nSchedule = new NSchedule(null, "제목", "메모", null, null, null, member.getEmail(), 0);

        when(nScheduleRepository.findById(any())).thenReturn(Optional.of(nSchedule));
        doNothing().when(nScheduleRepository).update(any());

        //when
        nScheduleService.update(member, nScheduleUpdate);

        //then
        assertThat(nSchedule.getTitle()).isEqualTo(nScheduleUpdate.getTitle());
        assertThat(nSchedule.getCommonDescription()).isEqualTo(nScheduleUpdate.getCommonDescription());
    }
}