package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotEmpty;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class NormalAddDto extends ScheduleAddDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime bufferTime;
    private LocalTime performInDay;
    @NotEmpty
    private Boolean isIncludeSaturday;
    @NotEmpty
    private Boolean isIncludeSunday;
    @NotEmpty
    private Integer totalAmount;
    @NotEmpty
    private Integer performInWeek;

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
