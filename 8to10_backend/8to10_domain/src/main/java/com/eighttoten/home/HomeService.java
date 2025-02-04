package com.eighttoten.home;

import com.eighttoten.achievement.Achievement;
import com.eighttoten.achievement.AchievementRepository;
import com.eighttoten.member.domain.Member;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final AchievementRepository achievementRepository;

    @Transactional(readOnly = true)
    public UserStats getDailyUserStats(Member member){
        Achievement achievement = achievementRepository
                .findByMemberIdAndDate(member.getId(), LocalDate.now())
                .orElse(null);

        if(achievement != null) {
            return UserStats.of(member.getNickname(), member.getRole().getValue(),
                    achievement.getAchievementRate());
        }
        return UserStats.of(member.getNickname(), member.getRole().getValue(), 0);
    }
}