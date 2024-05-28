package show.schedulemanagement.dto.schedule.request;

import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class NormalAddDto extends ScheduleAddDto {
    private LocalTime bufferTime;
    private String frequency;
    private List<String> days;
    private Integer totalValue;
}
