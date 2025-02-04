package com.eighttoten.home;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserStats {
    private String nickname;
    private String role;
    private double achievementRate;

    public static UserStats of(String nickname, String role, double achievementRate) {
        UserStats userStats = new UserStats();
        userStats.nickname = nickname;
        userStats.role= role;
        userStats.achievementRate = achievementRate;
        return userStats;
    }
}
