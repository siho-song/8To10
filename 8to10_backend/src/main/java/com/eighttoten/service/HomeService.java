package com.eighttoten.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.achievement.Achievement;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.UserStatsResponse;
import com.eighttoten.service.achievement.AchievementService;

@Service
@RequiredArgsConstructor
@Slf4j
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
