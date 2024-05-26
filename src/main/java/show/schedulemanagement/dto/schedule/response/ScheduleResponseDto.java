package show.schedulemanagement.dto.schedule.response;

import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class ScheduleResponseDto {

    private Long scheduleId;
    private String title;
    private String commonDescription;
    private LocalDateTime start; //2023-01-02T11:00:00
    private LocalDateTime end; // 2023-01-02T11:00:00
    private String type; // "N","F","V"
    private String color;

    protected ScheduleResponseDto(
            Long scheduleId,
            String title,
            String commonDescription,
            LocalDateTime start,
            LocalDateTime end,
            String type,
            String color
    ) {

        this.scheduleId = scheduleId;
        this.title = title;
        this.commonDescription = commonDescription;
        this.start = start;
        this.end = end;
        this.type = type;
        this.color = color;
    }
}
