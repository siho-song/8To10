package show.schedulemanagement.service.notification;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.notification.Notification;
import show.schedulemanagement.repository.notification.NotificationRepository;
import show.schedulemanagement.service.member.MemberService;

@SpringBootTest
class NotificationServiceTest {

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
    void delete(){
        //given
        Member member = memberService.getAuthenticatedMember();
        Long notificationId = 1L;
        //when
        notificationService.deleteById(member,notificationId);

        //then
        Notification result = notificationRepository.findById(notificationId).orElse(null);
        assertThat(result).isNull();
    }
}