package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
public class VariableRequestDto extends ScheduleRequestDto {
    private LocalDateTime start; //2023-01-02T11:00:00
    private LocalDateTime end; // 2023-01-02T11:00:00

    public VariableRequestDto(String title, String description,
                              String type, LocalDateTime start, LocalDateTime end) {
        super(title, description, type);
        this.start = start;
        this.end = end;
    }
}
