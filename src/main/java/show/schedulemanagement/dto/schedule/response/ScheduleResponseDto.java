package show.schedulemanagement.dto.schedule.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;

@SuperBuilder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ScheduleResponseDto {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime start; //2023-01-02T11:00:00
    private LocalDateTime end; // 2023-01-02T11:00:00
    private String type; // "normal","fixed","variable"
    private String color;
    private Boolean completeStatue;

    public static ScheduleResponseDto from(Schedule schedule, ScheduleAble scheduleAble) {
        if (schedule instanceof NSchedule) {
            return new NScheduleResponse((NSchedule) schedule, (NScheduleDetail) scheduleAble);
        } else if (schedule instanceof FSchedule) {
            return new FScheduleResponse((FSchedule) schedule,(FScheduleDetail) scheduleAble);
        } else if (schedule instanceof VSchedule) {
            return new VScheduleResponse((VSchedule) schedule);
        } else {
            throw new IllegalArgumentException("Unknown schedule type");
        }
    }
}
