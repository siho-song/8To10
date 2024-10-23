package show.schedulemanagement.service.achievement;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@Transactional
@DisplayName("성취도 서비스 테스트")
class AchievementServiceTest {

    @Autowired
    AchievementService achievementService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("월간 성취도 조회")
    @WithUserDetails(value = "normal@example.com")
    void findAllBetweenYearAndMonth() {
        Member member = memberService.getAuthenticatedMember();
        List<Achievement> achievements = achievementService.findAllBetweenYearAndMonth(member, 2024, 1);
        Assertions.assertThat(achievements.size()).isEqualTo(10);
    }
}