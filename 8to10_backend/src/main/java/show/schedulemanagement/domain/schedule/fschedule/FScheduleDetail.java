package show.schedulemanagement.domain.schedule.fschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import show.schedulemanagement.domain.auditing.baseentity.BaseEntity;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleDetailUpdate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@ToString(exclude = {"fSchedule"})
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

    public void update(FScheduleDetailUpdate fScheduleDetailUpdate){
        detailDescription = fScheduleDetailUpdate.getDetailDescription();
        startDate = fScheduleDetailUpdate.getStartDate();
        endDate = fScheduleDetailUpdate.getEndDate();
    }
}