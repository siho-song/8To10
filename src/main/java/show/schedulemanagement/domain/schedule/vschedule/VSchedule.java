package show.schedulemanagement.domain.schedule.vschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "V")
public class VSchedule extends Schedule{
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