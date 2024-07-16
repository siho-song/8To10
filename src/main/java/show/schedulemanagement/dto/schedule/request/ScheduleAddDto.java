package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
public abstract class ScheduleAddDto {
    @NotBlank
    @Size(min = 1,max = 80)
    private String title;
    
    private String commonDescription;

    protected ScheduleAddDto(String title, String commonDescription) {
        this.title = title;
        this.commonDescription = commonDescription;
    }
}
