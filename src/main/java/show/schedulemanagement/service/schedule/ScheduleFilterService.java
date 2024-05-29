package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;

@Service
@Slf4j
public class ScheduleFilterService {

    public Map<LocalDate, List<ScheduleAble>> filterScheduleByTime(List<Schedule> schedules, LocalTime wakeUpTime, LocalTime bedTime) {
        log.debug("filterScheduleByTime schedules : {}", schedules);
        log.debug("filterScheduleByTime wakeUpTime : {}", wakeUpTime);
        log.debug("filterScheduleByTime bedTime : {}", bedTime);

        Map<LocalDate, List<ScheduleAble>> result = schedules.stream()
                .flatMap(schedule -> schedule.getScheduleAbles().stream())
                .filter(scheduleAble -> {
                    LocalTime startTime = scheduleAble.getStartDate().toLocalTime().minusSeconds(1);
                    LocalTime endTime = scheduleAble.getEndDate().toLocalTime().plusSeconds(1);
                    log.debug("Filtering ScheduleAble startTime, endTime: {} , {} ", startTime, endTime);
                    boolean isValid;

                    if (bedTime.isBefore(wakeUpTime)) { // 취침 시간이 다음 날로 이어지는 경우
                        isValid = (!startTime.isBefore(wakeUpTime) || !startTime.isAfter(bedTime))
                                && (!endTime.isBefore(wakeUpTime) || !endTime.isAfter(bedTime));
                    } else { // 기상 시간과 취침 시간이 같은 날 내에 있는 경우
                        isValid = !startTime.isBefore(wakeUpTime) && !endTime.isAfter(bedTime);
                    }

                    log.debug("Filtering ScheduleAble: {} -> {}", scheduleAble, isValid);
                    return isValid;
                })
                .collect(Collectors.groupingBy(scheduleAble -> scheduleAble.getStartDate().toLocalDate(),
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            list.sort(Comparator.comparing(ScheduleAble::getStartDate));
                            return list;
                        })));

        log.debug("Result after grouping and filtering: {}", result);

        // Empty lists check
        result.forEach((date, list) -> log.debug("Date: {}, List size: {}", date, list.size()));
        return result;
    }
}
