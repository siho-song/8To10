package com.eighttoten.service.achievement;

import com.eighttoten.achievement.Achievement;
import com.eighttoten.achievement.AchievementService;
import com.eighttoten.member.domain.Member;
import com.eighttoten.service.MemberDetailsService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DisplayName("성취도 서비스 테스트")
class AchievementEntityServiceTest {

    @Autowired
    AchievementService achievementService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("월간 성취도 조회")
    @WithUserDetails(value = "normal@example.com")
    void findAllBetweenYearAndMonth() {
        Member member = memberDetailsService.getAuthenticatedMember();
        List<Achievement> achievements = achievementService.getMonthlyAchievement(member.getId(), 2024, 1);
        Assertions.assertThat(achievements.size()).isEqualTo(10);
    }
}