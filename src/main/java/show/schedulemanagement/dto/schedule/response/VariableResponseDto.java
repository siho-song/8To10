package show.schedulemanagement.dto.schedule.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.schedule.ScheduleColor;

@SuperBuilder
@ToString
@Getter
public class VariableResponseDto extends ScheduleResponseDto {

    Boolean completeStatus;

    public VariableResponseDto(VSchedule schedule) {
        super(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getDescription(),
                LocalDateTime.of(schedule.getStartDate(),schedule.getStartTime()),
                LocalDateTime.of(schedule.getEndDate(),schedule.getEndTime()),
                "variable",
                ScheduleColor.RED.name()
                );

        completeStatus = false;
    }
}
