package com.eighttoten.home.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.achievement.Achievement;
import com.eighttoten.achievement.AchievementRepository;
import com.eighttoten.home.HomeService;
import com.eighttoten.home.UserStats;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.Role;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("홈 서비스 테스트")
class HomeServiceTest {
    @MockBean
    AchievementRepository achievementRepository;

    @Autowired
    HomeService homeService;

    @Test
    @DisplayName("유저의 당일 성취도가 성취도 테이블에 없으면 성취도의 값은 0이다.")
    void getDailyUserStats_achievement_is_null() {
        //given
        Member member = new Member(null, null, "테스트 닉네임",
                null, null, null, null,
                Role.NORMAL_USER, null, null, 0, false, false);

        when(achievementRepository.findByMemberIdAndDate(any(), any()))
                .thenReturn(Optional.empty());

        UserStats userStats = homeService.getDailyUserStats(member);

        //then
        assertThat(userStats.getAchievementRate()).isEqualTo(0d);
        assertThat(userStats.getRole()).isEqualTo(member.getRole().getValue());
        assertThat(userStats.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("유저의 당일 성취도가 성취도 테이블에 존재하면 성취도의 값을 반환한다.")
    void getDailyUserStats_achievement_not_null(){
        //given
        Member member = new Member(null, null, "테스트 닉네임",
                null, null, null, null,
                Role.NORMAL_USER, null, null, 0, false, false);

        Achievement achievement = new Achievement(null,member.getId(),LocalDate.now(),0.8);
        when(achievementRepository.findByMemberIdAndDate(any(), any()))
                .thenReturn(Optional.of(achievement));

        //when
        UserStats userStats = homeService.getDailyUserStats(member);

        //then
        assertThat(userStats.getAchievementRate()).isEqualTo(0.8);
        assertThat(userStats.getRole()).isEqualTo(member.getRole().getValue());
        assertThat(userStats.getNickname()).isEqualTo(member.getNickname());
    }
}