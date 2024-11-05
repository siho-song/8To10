package show.schedulemanagement.dto.achievement;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import show.schedulemanagement.domain.achievement.Achievement;

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
