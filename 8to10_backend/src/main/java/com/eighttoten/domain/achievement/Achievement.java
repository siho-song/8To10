package com.eighttoten.domain.achievement;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.domain.auditing.baseentity.BaseEntity;
import com.eighttoten.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Achievement extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "achievement_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate achievementDate;

    private double achievementRate;

    public static Achievement createAchievement(
            Member member,
            LocalDate achievementDate,
            double achievementRate)
    {
        Achievement achievement = new Achievement();
        achievement.member = member;
        achievement.achievementDate = achievementDate;
        achievement.achievementRate = achievementRate;
        return achievement;
    }
}