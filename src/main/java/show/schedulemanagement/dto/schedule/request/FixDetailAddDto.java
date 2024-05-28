package show.schedulemanagement.dto.schedule.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import show.schedulemanagement.validator.schedule.filedError.Day;
import show.schedulemanagement.validator.schedule.filedError.Frequency;
import show.schedulemanagement.validator.schedule.filedError.UniqueDayList;

@Getter
@Builder
@ToString
public class FixDetailAddDto {
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime duration;
    @Frequency
    private String frequency;

    @NotNull
    @UniqueDayList
    private List<@Day String> days;
}
