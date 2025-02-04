package com.eighttoten.achievement;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
    private Long id;
    private Long memberId;
    private LocalDate achievementDate;
    private double achievementRate;

    public void updateAchievementRate(double achievementRate){
        this.achievementRate = achievementRate;
    }
}