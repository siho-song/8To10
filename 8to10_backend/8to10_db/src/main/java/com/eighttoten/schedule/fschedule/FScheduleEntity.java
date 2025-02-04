package com.eighttoten.schedule.fschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "f_schedule")
public class FScheduleEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "f_schedule_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT DEFAULT ''")
    private String commonDescription;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    public static FScheduleEntity from(NewFSchedule newFSchedule, MemberEntity memberEntity){
        FScheduleEntity fScheduleEntity = new FScheduleEntity();
        fScheduleEntity.memberEntity = memberEntity;
        fScheduleEntity.title = newFSchedule.getTitle();
        fScheduleEntity.commonDescription = newFSchedule.getCommonDescription();
        fScheduleEntity.startDate = newFSchedule.getStartDate();
        fScheduleEntity.endDate = newFSchedule.getEndDate();
        return fScheduleEntity;
    }

    public FSchedule toFSchedule() {
        return new FSchedule(id, title, commonDescription, startDate, endDate, createdBy);
    }

    public void update(String title, String commonDescription) {
        this.title = title;
        this.commonDescription = commonDescription;
    }
}