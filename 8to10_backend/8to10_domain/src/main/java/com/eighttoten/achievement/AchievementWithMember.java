package com.eighttoten.achievement;

import com.eighttoten.member.domain.Member;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AchievementWithMember {
    private Long id;
    private Member member;
    private LocalDate achievementDate;
    private double achievementRate;
}
