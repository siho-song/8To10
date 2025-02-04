package com.eighttoten.achievement;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class NewAchievement {
    private Long memberId;
    private LocalDate achievementDate;
    private double achievementRate;

    public static NewAchievement from(Long memberId, LocalDate achievementDate, double achievementRate){
        NewAchievement newAchievement = new NewAchievement();
        newAchievement.memberId = memberId;
        newAchievement.achievementDate = achievementDate;
        newAchievement.achievementRate = achievementRate;
        return newAchievement;
    }
}
