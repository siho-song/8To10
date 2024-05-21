package show.schedulemanagement.domain.schedule.fschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "F_SCHEDULE_DETAIL")
public class FScheduleDetail {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "f_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "f_schedule_id")
    private FSchedule fSchedule;

    private String day;
    private boolean completeStatus;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    protected FScheduleDetail(){
        completeStatus = false;
    }
}