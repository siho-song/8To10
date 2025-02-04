package com.eighttoten.service.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import com.eighttoten.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.member.service.MemberService;

@SpringBootTest
@DisplayName("알림 서비스 테스트")
class NotificationEntityServiceTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("알림 삭제 요청을 날린 유저와 , 알림의 유저가 같으면 알림을 삭제한다.")
    @WithUserDetails("normal@example.com")
    @Transactional
    void delete() {
        //given
        Member member = memberService.getAuthenticatedMember();
        Long notificationId = 1L;
        //when
        notificationService.deleteById(member, notificationId);

        //then
        Notification result = notificationRepository.findById(notificationId).orElse(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("알림 읽음상태 변경 요청을 날린 유저와, 알림의 유저가 같으면 읽음 상태를 읽음으로 변경한다.")
    @WithUserDetails("normal@example.com")
    void update(){
        //given
        Member member = memberService.getAuthenticatedMember();
        Long notificationId = 1L;
        Boolean readStatus = true;

        //when
        notificationService.updateReadStatus(member, notificationId, readStatus);

        //then
        Notification result = notificationRepository.findById(notificationId)
                .orElseThrow(EntityNotFoundException::new);

        assertThat(result.isReadStatus()).isTrue();
    }
}