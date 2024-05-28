package show.schedulemanagement.dto.schedule.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.ScheduleColor;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class FixResponseDto extends ScheduleResponseDto {
    private Long parentId;

    public FixResponseDto(FSchedule fSchedule, FScheduleDetail fScheduleDetail) {
        super(fScheduleDetail.getId(),
                fSchedule.getTitle(),
                fSchedule.getCommonDescription(),
                fScheduleDetail.getStartDate(),
                fScheduleDetail.getEndDate(),
                "fixed",
                ScheduleColor.FIXED.name(),
                fScheduleDetail.isCompleteStatus());

        this.parentId = fSchedule.getId();
    }
}
