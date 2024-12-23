package com.eighttoten.home;

import com.eighttoten.achievement.domain.Achievement;
import com.eighttoten.achievement.service.AchievementService;
import com.eighttoten.member.domain.Member;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final AchievementService achievementService;

    @Transactional(readOnly = true)
    public UserStatsResponse getDailyUserStats(Member member){
        Achievement achievement = achievementService.findByMemberAndDateIfExists(member, LocalDate.now());
        if(achievement != null) {
            return UserStatsResponse.of(member.getNickname(), member.getRole().getValue(),
                    achievement.getAchievementRate());
        }
        return UserStatsResponse.of(member.getNickname(), member.getRole().getValue(), 0);
    }
}