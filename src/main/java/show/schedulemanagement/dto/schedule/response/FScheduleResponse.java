package show.schedulemanagement.dto.schedule.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.ScheduleColor;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class FScheduleResponse extends ScheduleResponse {
    private Long parentId;
    public FScheduleResponse(FSchedule fSchedule, FScheduleDetail fScheduleDetail) {
        super(fScheduleDetail.getId(),
                fSchedule.getTitle(),
                fSchedule.getCommonDescription(),
                fScheduleDetail.getStartDate(),
                fScheduleDetail.getEndDate(),
                "fixed",
                ScheduleColor.FIXED.hexCode,
                fScheduleDetail.isCompleteStatus());

        this.parentId = fSchedule.getId();
    }
}
