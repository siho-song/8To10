package com.eighttoten.dto.achievement;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import com.eighttoten.domain.achievement.Achievement;

@Getter
@Builder
public class AchievementResponse {
    private LocalDate achievementDate;
    private double achievementRate;

    public static AchievementResponse from(Achievement achievement){
        return AchievementResponse.builder()
                .achievementDate(achievement.getAchievementDate())
                .achievementRate(achievement.getAchievementRate())
                .build();
    }
}
