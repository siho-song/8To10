package com.eighttoten.schedule.service.nschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.nschedule.NDetailUpdate;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("일반일정 자식일정 서비스 테스트")
class NScheduleDetailServiceTest {
    @MockBean
    NScheduleDetailRepository nScheduleDetailRepository;
    
    @Autowired
    NScheduleDetailService nScheduleDetailService;

    @Test
    @DisplayName("일반일정 자식일정 단건 수정")
    void update() {
        //given
        Member member = TestDataUtils.createTestMember(1L,"normal@example.com");
        NDetailUpdate nDetailUpdate = new NDetailUpdate(1L, "수정된 메모");
        NScheduleDetail nScheduleDetail = new NScheduleDetail(1L, null, null, null, null, null, member.getEmail(),
                false, 0, 0);

        when(nScheduleDetailRepository.findById(any())).thenReturn(Optional.of(nScheduleDetail));
        doNothing().when(nScheduleDetailRepository).update(any());

        //when
        nScheduleDetailService.update(member, nDetailUpdate);

        //then
        assertThat(nScheduleDetail.getDetailDescription()).isEqualTo(nDetailUpdate.getDetailDescription());
    }
}