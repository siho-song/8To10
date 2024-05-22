package show.schedulemanagement.domain.schedule.nschedule;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.CategoryUnit;
import show.schedulemanagement.domain.schedule.Schedule;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@DiscriminatorValue(value = "N")
public class NSchedule extends Schedule{
    @Enumerated(value = STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "NONE")
    private CategoryUnit categoryUnit;

    @Column(nullable = false)
    @ColumnDefault(value = "00:00:00")
    private LocalTime bufferTime;

    @Column(nullable = false)
    private String frequency; //매일 , 매주 , 매월
}