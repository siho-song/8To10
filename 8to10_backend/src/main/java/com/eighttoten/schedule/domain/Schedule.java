package com.eighttoten.schedule.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.global.auditing.baseentity.BaseEntity;
import com.eighttoten.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class Schedule extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "schedule_id")
    protected Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    @Column(nullable = false)
    protected String title;

    @Column(columnDefinition = "TEXT DEFAULT ''")
    protected String commonDescription;

    @Column(nullable = false)
    protected LocalDateTime startDate;

    @Column(nullable = false)
    protected LocalDateTime endDate;

    public abstract List<ScheduleAble> getScheduleAbles();
}