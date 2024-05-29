package show.schedulemanagement.dto.schedule.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.schedule.ScheduleColor;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class VariableResponseDto extends ScheduleResponseDto {

    public VariableResponseDto(VSchedule schedule) {
        super(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getCommonDescription(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                "variable",
                ScheduleColor.VARIABLE.hexCode,
                schedule.isCompleteStatus()
        );
    }
}
