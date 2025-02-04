package com.eighttoten.schedule.fschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFScheduleDetail;
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
public class FScheduleDetailEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "f_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "f_schedule_id", nullable = false)
    private FScheduleEntity fScheduleEntity;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    public void update(String detailDescription,LocalDateTime start, LocalDateTime end){
        this.detailDescription = detailDescription;
        this.startDateTime = start;
        this.endDateTime = end;
    }

    public static FScheduleDetailEntity from(NewFScheduleDetail newFScheduleDetail, FScheduleEntity fScheduleEntity) {
        return new FScheduleDetailEntity(null,
                fScheduleEntity,
                newFScheduleDetail.getDetailDescription(),
                newFScheduleDetail.getStartDateTime(),
                newFScheduleDetail.getEndDateTime());
    }

    public FScheduleDetail toFScheduleDetail(){
        return new FScheduleDetail(
                id, fScheduleEntity.getId(),
                detailDescription, startDateTime, endDateTime, createdBy);
    }

    public FDetailWithParent toFDetailWithParent(){
        return new FDetailWithParent(id, fScheduleEntity.toFSchedule(), detailDescription, startDateTime, endDateTime,
                createdBy);
    }
}