package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotEmpty;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class NormalAddDto extends ScheduleAddDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime bufferTime;
    private LocalTime performInDay;
    private boolean isIncludeSaturday;
    private boolean isIncludeSunday;
    private int totalAmount;
    private int performInWeek;

    public List<DayOfWeek> getCandidateDays() {
        List<DayOfWeek> candidateDays = new ArrayList<>(
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY));
        if(isIncludeSaturday) {
            candidateDays.add(DayOfWeek.SATURDAY);
        }
        if (isIncludeSunday) {
            candidateDays.add(DayOfWeek.SUNDAY);
        }
        return candidateDays;
    }

    public LocalTime getNecessaryTime() {
        return bufferTime.plusHours(performInDay.getHour()).plusMinutes(performInDay.getMinute());
    }
}
