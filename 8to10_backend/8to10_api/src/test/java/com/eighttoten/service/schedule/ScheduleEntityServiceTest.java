package com.eighttoten.service.schedule;

import static org.assertj.core.api.Assertions.*;

import com.eighttoten.schedule.service.ScheduleAbleService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.member.service.MemberService;

@SpringBootTest
@DisplayName("일정 공통 서비스 테스트")
@Transactional
class ScheduleEntityServiceTest {

    @Autowired
    ScheduleAbleService scheduleAbleService;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("일반일정 삭제 - 자식일정까지 함께 삭제")
    void delete_NSchedule() {
        MemberEntity memberEntity = memberService.getAuthenticatedMember();
        Long scheduleId = 4L;
        scheduleAbleService.deleteById(memberEntity, scheduleId);
        assertThatThrownBy(()-> scheduleAbleService.findById(scheduleId)).isInstanceOf(RuntimeException.class);

        List<NScheduleDetailEntity> nScheduleDetailEntities = entityManager.createQuery(
                        "select nd from NScheduleDetailEntity nd where nd.nSchedule.id = :scheduleId", NScheduleDetailEntity.class)
                .setParameter("scheduleId", scheduleId)
                .getResultList();

        assertThat(nScheduleDetailEntities.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("변동일정 삭제")
    void delete_VSchedule(){
        MemberEntity memberEntity = memberService.getAuthenticatedMember();
        Long scheduleId = 7L;
        scheduleAbleService.deleteById(memberEntity, scheduleId);
        assertThatThrownBy(()-> scheduleAbleService.findById(scheduleId)).isInstanceOf(RuntimeException.class);
    }
}