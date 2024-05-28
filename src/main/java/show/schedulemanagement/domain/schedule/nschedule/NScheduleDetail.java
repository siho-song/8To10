package show.schedulemanagement.domain.schedule.nSchedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.auditing.baseEntity.BaseUpdatedEntity;
import show.schedulemanagement.domain.schedule.ScheduleAble;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
public class NScheduleDetail extends BaseUpdatedEntity implements ScheduleAble {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private NSchedule nSchedule;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;

    @Column(nullable = false)
    private String day;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;
}