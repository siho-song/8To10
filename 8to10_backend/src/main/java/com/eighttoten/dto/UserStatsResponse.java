package com.eighttoten.dto;

import lombok.Getter;

@Getter
public class UserStatsResponse {
    private String nickname;
    private String role;
    private double achievementRate;

    public static UserStatsResponse of(String nickname, String role, double achievementRate) {
        UserStatsResponse statsResponse = new UserStatsResponse();
        statsResponse.nickname = nickname;
        statsResponse.role= role;
        statsResponse.achievementRate = achievementRate;
        return statsResponse;
    }
}