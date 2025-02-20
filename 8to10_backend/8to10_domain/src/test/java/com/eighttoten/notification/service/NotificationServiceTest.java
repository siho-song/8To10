package com.eighttoten.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("알림 서비스 테스트")
class NotificationServiceTest {

    @MockBean
    NotificationRepository notificationRepository;

    @Autowired
    NotificationService notificationService;

    @Test
    @DisplayName("알림 읽음상태 변경 요청을 날린 유저와, 알림의 유저가 같으면 읽음 상태를 읽음으로 변경한다.")
    void readStatus_update(){
        //given
        Member member = TestDataUtils.createTestMember(1L,"test@test.com");
        boolean newReadStatus = true;
        Notification notification = new Notification(1L, null, null, null,
                null, false, member.getEmail());

        //when
        when(notificationRepository.findById(any())).thenReturn(Optional.of(notification));
        doNothing().when(notificationRepository).update(any());
        notificationService.updateReadStatus(member, notification.getId(), newReadStatus);

        //then
        assertThat(notification.isReadStatus()).isTrue();
    }
}