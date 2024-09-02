package show.schedulemanagement.domain.schedule.fSchedule;

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
import show.schedulemanagement.domain.auditing.baseEntity.BaseUpdatedEntity;
import show.schedulemanagement.domain.schedule.ScheduleAble;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@ToString(exclude = {"fSchedule"})
public class FScheduleDetail extends BaseUpdatedEntity implements ScheduleAble {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "f_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private FSchedule fSchedule;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    public static FScheduleDetail createFscheduleDetail(LocalDateTime startDate, LocalDateTime endDate){
        FScheduleDetail fScheduleDetail = new FScheduleDetail();
        fScheduleDetail.completeStatus = false;
        fScheduleDetail.startDate = startDate;
        fScheduleDetail.endDate = endDate;
        return fScheduleDetail;
    }

    public void setFSchedule(FSchedule fSchedule) {
        this.fSchedule = fSchedule;
        fSchedule.getFScheduleDetails().add(this);
    }
}