package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "N_SCHEDULE")
public class NSchedule{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_id")
    private Long id;

    @Enumerated(value = STRING)
    private CategoryUnit categoryUnit;
    private LocalTime bufferTime;
    private String frequency; //매일 , 매주 , 매월

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    protected NSchedule() {
        categoryUnit = CategoryUnit.NONE;
        bufferTime = LocalTime.of(0, 0, 0);
    }
}