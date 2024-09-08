package show.schedulemanagement.dto.schedule.request.fSchedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import show.schedulemanagement.validator.schedule.filedError.Day;
import show.schedulemanagement.validator.schedule.filedError.Frequency;
import show.schedulemanagement.validator.schedule.filedError.PerformInDay;
import show.schedulemanagement.validator.schedule.filedError.UniqueDayList;
import show.schedulemanagement.validator.schedule.filedError.ZeroSeconds;

@Getter
@Builder
@ToString
public class FScheduleDetailSave {
    @ZeroSeconds
    private LocalTime startTime;

    @ZeroSeconds
    @PerformInDay
    private LocalTime duration;

    @Frequency
    @NotNull
    private String frequency;

    @NotNull
    @UniqueDayList
    private List<@Day String> days;
}
