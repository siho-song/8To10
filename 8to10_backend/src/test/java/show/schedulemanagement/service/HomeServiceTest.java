package show.schedulemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.UserStatsResponse;
import show.schedulemanagement.service.achievement.AchievementService;
import show.schedulemanagement.service.member.MemberService;

@Slf4j
@SpringBootTest
class HomeServiceTest {

    @Autowired
    MemberService memberService;

    @MockBean
    AchievementService achievementService;

    @Autowired
    HomeService homeService;

    @Test
    @WithUserDetails("normal@example.com")
    @Transactional
    @DisplayName("유저의 당일 성취도가 성취도 테이블에 없으면 성취도의 값은 0이다.")
    void getDailyUserStats_achievement_is_null() {
        //given
        Member member = memberService.getAuthenticatedMember();
        when(achievementService.findByMemberAndDateIfExists(eq(member), any()))
                .thenReturn(null);

        //when
        UserStatsResponse response = homeService.getDailyUserStats(member);

        //then
        assertThat(response.getAchievementRate()).isEqualTo(0d);
    }

    @Test
    @WithUserDetails("normal@example.com")
    @Transactional
    @DisplayName("유저의 당일 성취도가 성취도 테이블에 존재하면 성취도의 값을 반환한다.")
    void getDailyUserStats_achievement_not_null(){
        //given
        Member member = memberService.getAuthenticatedMember();
        double achievementRate = 0.1;
        when(achievementService.findByMemberAndDateIfExists(eq(member), any()))
                .thenReturn(Achievement.createAchievement(member, LocalDate.now(), achievementRate));

        //when
        UserStatsResponse response = homeService.getDailyUserStats(member);

        //then
        assertThat(response.getAchievementRate()).isEqualTo(achievementRate);
    }
}