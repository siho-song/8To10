package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
public class NSchedule{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_id")
    private Long id;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "NONE")
    private CategoryUnit categoryUnit;

    @Column(nullable = false)
    @ColumnDefault(value = "00:00:00")
    private LocalTime bufferTime;

    @Column(nullable = false)
    private String frequency; //매일 , 매주 , 매월

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

}