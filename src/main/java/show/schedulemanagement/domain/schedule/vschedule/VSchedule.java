package show.schedulemanagement.domain.schedule.vschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
public class VSchedule extends BaseUpdatedEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "v_schedule_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;
}