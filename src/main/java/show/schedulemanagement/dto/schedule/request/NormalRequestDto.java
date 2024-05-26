package show.schedulemanagement.dto.schedule.request;

import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.CategoryUnit;

@SuperBuilder
@Getter
public class NormalRequestDto extends ScheduleRequestDto {
    private CategoryUnit categoryUnit;
    private LocalTime bufferTime;
    private String frequency;
    private List<String> days;
    private Integer startRange;
    private Integer endRange;
    private Integer totalValue;

    public NormalRequestDto(String title, String description,
                            String dtype, CategoryUnit categoryUnit, LocalTime bufferTime, String frequency,
                            List<String> days, Integer startRange, Integer endRange, Integer totalValue) {
        super(title, description, dtype);
        this.categoryUnit = categoryUnit;
        this.bufferTime = bufferTime;
        this.frequency = frequency;
        this.days = days;
        this.startRange = startRange;
        this.endRange = endRange;
        this.totalValue = totalValue;
    }
}
