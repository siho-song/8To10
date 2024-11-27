package com.eighttoten.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
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
