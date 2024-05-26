package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.validator.ScheduleType;

@Getter
@SuperBuilder
@ToString
public abstract class ScheduleRequestDto {
    @NotBlank
    @Size(min = 1,max = 80)
    private String title;

    @NotBlank
    private String commonDescription;

    @ScheduleType
    private String type; // "variable","fixed","normal"

    protected ScheduleRequestDto(String title, String commonDescription, String type) {
        this.title = title;
        this.commonDescription = commonDescription;
        this.type = type;
    }
}
