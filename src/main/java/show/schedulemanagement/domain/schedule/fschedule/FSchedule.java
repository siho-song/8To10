package show.schedulemanagement.domain.schedule.fschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@DiscriminatorValue(value = "F")
public class FSchedule extends Schedule{
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String frequency;
}