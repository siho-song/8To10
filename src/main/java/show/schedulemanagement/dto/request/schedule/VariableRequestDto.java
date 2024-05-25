package show.schedulemanagement.dto.request.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
@Getter
public class VariableRequestDto extends ScheduleRequestDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;

    public VariableRequestDto(String title, String description, LocalDate startDate, LocalDate endDate,
                              String dtype, LocalTime startTime, LocalTime endTime, String day) {
        super(title, description, startDate, endDate, dtype);
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }
}
