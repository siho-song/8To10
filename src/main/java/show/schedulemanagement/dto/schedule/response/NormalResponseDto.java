package show.schedulemanagement.dto.schedule.response;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class NormalResponseDto extends ScheduleResponseDto {
    private Long parentId;
    private Integer dailyAmount;
}
