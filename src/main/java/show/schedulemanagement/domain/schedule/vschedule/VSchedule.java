package show.schedulemanagement.domain.schedule.vschedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "V_SCHEDULE")
public class VSchedule {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "v_schedule_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private LocalTime startTime;
    private LocalTime endTime;
    private String day;
    private boolean completeStatus;

    protected VSchedule(){
        completeStatus = false;
    }
}