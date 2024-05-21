package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "N_SCHEDULE_DETAIL")
public class NScheduleDetail{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "n_schedule_id")
    private NSchedule nSchedule;

    private LocalTime startTime;
    private LocalTime endTime;
    private boolean completeStatus;
    private String day;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    protected NScheduleDetail() {
        completeStatus = false;
    }
}