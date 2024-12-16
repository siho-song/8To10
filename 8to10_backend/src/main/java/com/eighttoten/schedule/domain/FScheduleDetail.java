package com.eighttoten.schedule.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.global.auditing.baseentity.BaseEntity;
import com.eighttoten.schedule.dto.request.FixDetailUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "f_schedule_detail")
public class FScheduleDetail extends BaseEntity implements ScheduleAble {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "f_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private FSchedule fSchedule;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    public static FScheduleDetail createFscheduleDetail(LocalDateTime startDate, LocalDateTime endDate){
        FScheduleDetail fScheduleDetail = new FScheduleDetail();
        fScheduleDetail.startDate = startDate;
        fScheduleDetail.endDate = endDate;
        return fScheduleDetail;
    }

    public void setFSchedule(FSchedule fSchedule) {
        this.fSchedule = fSchedule;
        fSchedule.getFScheduleDetails().add(this);
    }

    public void update(FixDetailUpdateRequest dto){
        if(dto.getDetailDescription() != null){
            detailDescription = dto.getDetailDescription();
        }
        startDate = dto.getStartDate();
        endDate = dto.getEndDate();
    }
}