package show.schedulemanagement.dto.schedule.request;

import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class FixRequestDto extends ScheduleRequestDto {
    private LocalTime startTime;
    private LocalTime duration;
    private String frequency;
    private List<String> days;

    public FixRequestDto(String title, String description, String type,
                         LocalTime startTime, LocalTime duration, String frequency, List<String> days) {
        super(title, description, type);
        this.startTime = startTime;
        this.duration = duration;
        this.frequency = frequency;
        this.days = days;
    }
}
