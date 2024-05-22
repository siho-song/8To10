package show.schedulemanagement.web.request.schedule;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public abstract class ScheduleRequestDto {
    private String title;
    private String description;
    private LocalDate startDate; //2023-01-02T11:00:00
    private LocalDate endDate; // 2023-01-02T11:00:00
    private String dtype; // "N","F","V"

    protected ScheduleRequestDto(String title, String description, LocalDate startDate, LocalDate endDate, String dtype) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dtype = dtype;
    }
}
