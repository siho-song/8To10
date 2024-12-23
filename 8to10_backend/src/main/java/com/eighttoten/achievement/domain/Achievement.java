package com.eighttoten.achievement.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.global.auditing.baseentity.BaseEntity;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.NScheduleDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
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
            LocalDate achievementDate)
    {
        Achievement achievement = new Achievement();
        achievement.member = member;
        achievement.achievementDate = achievementDate;
        return achievement;
    }

    public void setAchievementRate(List<NScheduleDetail> nScheduleDetails){
        double achievementSum = nScheduleDetails.stream()
                .mapToDouble(NScheduleDetail::getAchievementRate)
                .sum();

        int size = nScheduleDetails.size();
        this.achievementRate = achievementSum / size;
    }
}