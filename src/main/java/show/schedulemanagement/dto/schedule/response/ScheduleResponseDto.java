package show.schedulemanagement.dto.schedule.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
@Getter
@NoArgsConstructor
public abstract class ScheduleResponseDto {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime start; //2023-01-02T11:00:00
    private LocalDateTime end; // 2023-01-02T11:00:00
    private String type; // "normal","fixed","variable"
    private String color;
    private Boolean completeStatue;

    protected ScheduleResponseDto(
            Long id,
            String title,
            String commonDescription,
            LocalDateTime start,
            LocalDateTime end,
            String type,
            String color,
            Boolean completeStatue
    ) {

        this.id = id;
        this.title = title;
        this.commonDescription = commonDescription;
        this.start = start;
        this.end = end;
        this.type = type;
        this.color = color;
        this.completeStatue = completeStatue;
    }
}
