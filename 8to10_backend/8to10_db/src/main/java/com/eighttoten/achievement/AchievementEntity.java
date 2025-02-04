package com.eighttoten.achievement;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "achievement")
public class AchievementEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "achievement_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private MemberEntity memberEntity;

    @Column(nullable = false)
    private LocalDate achievementDate;

    private double achievementRate;

    public static AchievementEntity from(NewAchievement newAchievement, MemberEntity memberEntity) {
        AchievementEntity achievementEntity = new AchievementEntity();
        achievementEntity.memberEntity = memberEntity;
        achievementEntity.achievementDate = newAchievement.getAchievementDate();
        achievementEntity.achievementRate = newAchievement.getAchievementRate();
        return achievementEntity;
    }

    public Achievement toAchievement(){
        return new Achievement(id, memberEntity.getId(), achievementDate, achievementRate);
    }

    public AchievementWithMember toAchievementWithMember(){
        return new AchievementWithMember(id, memberEntity.toMember(), achievementDate, achievementRate);
    }

    public void update(Achievement achievement) {
        achievementRate = achievement.getAchievementRate();
    }
}