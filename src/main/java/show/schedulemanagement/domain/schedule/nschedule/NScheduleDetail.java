package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.baseEntity.BaseUpdatedEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
public class NScheduleDetail extends BaseUpdatedEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "n_schedule_id", nullable = false)
    private NSchedule nSchedule;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;

    @Column(nullable = false)
    private String day;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;
}