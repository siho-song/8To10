package com.eighttoten.home;

import lombok.Getter;

@Getter
public class UserStatsResponse {
    private String nickname;
    private String role;
    private double achievementRate;

    public static UserStatsResponse from(UserStats userStats) {
        UserStatsResponse statsResponse = new UserStatsResponse();
        statsResponse.nickname = userStats.getNickname();
        statsResponse.role= userStats.getRole();
        statsResponse.achievementRate = userStats.getAchievementRate();
        return statsResponse;
    }
}