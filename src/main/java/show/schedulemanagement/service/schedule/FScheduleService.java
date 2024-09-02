package show.schedulemanagement.service.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.schedule.ScheduleDay;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.request.FixAddDto;
import show.schedulemanagement.dto.schedule.request.FixDetailAddDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FScheduleService {

    @Transactional
    public void addDetailsForEachEvent(FSchedule fSchedule, List<FixDetailAddDto> events) {
        events.forEach(event -> addDetails(fSchedule, event));
    }

    private void addDetails(FSchedule schedule, FixDetailAddDto event) {

        LocalDate currentDate = schedule.getStartDate().toLocalDate();
        LocalDate endDate = schedule.getEndDate().toLocalDate();
        List<String> days = event.getDays();
        LocalTime duration = event.getDuration();
        LocalTime startTime = event.getStartTime();

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            for (String day : days) {
                if (currentDayOfWeek.equals(ScheduleDay.of(day))) {
                    LocalDateTime startDateTime = LocalDateTime.of(currentDate, startTime);
                    LocalDateTime endDateTime = startDateTime.plusHours(duration.getHour())
                            .plusMinutes(duration.getMinute());

                    FScheduleDetail fscheduleDetail = FScheduleDetail.createFscheduleDetail(startDateTime, endDateTime);
                    fscheduleDetail.setFSchedule(schedule);
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }
}
