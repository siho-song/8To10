package show.schedulemanagement.dto.schedule.response.fschedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.fschedule.FSchedule;
import show.schedulemanagement.domain.schedule.fschedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.ScheduleColor;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;

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
