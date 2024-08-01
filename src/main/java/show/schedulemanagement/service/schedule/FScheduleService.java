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
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.ScheduleDay;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.dto.schedule.request.FixAddDto;
import show.schedulemanagement.dto.schedule.request.FixDetailAddDto;
import show.schedulemanagement.dto.schedule.response.FixResponseDto;
import show.schedulemanagement.dto.schedule.response.Result;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FScheduleService {

    private final ScheduleService scheduleService;

    @Transactional
    public FSchedule addDetailsToFSchedule(Member member, FixAddDto dto) throws RuntimeException{
        FSchedule newSchedule = FSchedule.createFSchedule(member, dto);
        String commonDescription = dto.getCommonDescription();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        List<FixDetailAddDto> events = dto.getEvents();
        log.debug("addDetailsToFSchedule dto events : {} ",events);

        addDetailsByFrequency(newSchedule, events, commonDescription, startDate, endDate);

        log.debug("addDetailsToFSchedule after addWeeklyAndDailyDetails newSchedule : {} ",newSchedule);
        List<ScheduleAble> scheduleAbles = newSchedule.getScheduleAbles();
        log.debug("addDetailsToFSchedule after newSchedule scheduleAbles : {} ",scheduleAbles);

        //기간내 멤버의 모든 일정 불러오기
        List<Schedule> allSchedule = scheduleService.findAllWithinDates(member, startDate, endDate);
        //겹치는 일정이 있는지 검사
        Schedule conflictSchedule = scheduleService.getConflictSchedule(scheduleAbles,allSchedule);

        log.debug("addDetailsToFSchedule after getConflictSchedule about conflictSchedule: {} ",conflictSchedule);

        //TODO 일정이 겹칠떄
        if(conflictSchedule != null){
            if (conflictSchedule instanceof NSchedule) {
                //나중에 구현
                //TODO
                log.debug("conflictSchedule is NSchedule");
            } else {
                throw new RuntimeException("겹치는 일정을 조율해야합니다");
            }
        }

        return newSchedule;
    }

    public Result<FixResponseDto> getResult(FSchedule schedule) {
        List<FScheduleDetail> fScheduleDetails = schedule.getFScheduleDetails();
        Result<FixResponseDto> result = new Result<>();
        List<FixResponseDto> events = result.getEvents();

        for (FScheduleDetail fScheduleDetail : fScheduleDetails) {
            events.add(new FixResponseDto(schedule,fScheduleDetail));
        }
        log.debug("FScheduleService call getResult : {}",result);
        return result;
    }

    private void addDetailsByFrequency(FSchedule newSchedule, List<FixDetailAddDto> events, String commonDescription,
                                       LocalDate startDate, LocalDate endDate) {
        log.debug("addDetailsByFrequency : {}" , events);
        for (FixDetailAddDto event : events) {
            String frequency = event.getFrequency();
            List<String> days = event.getDays();
            //주기 체크
            if (frequency.equals("weekly") || frequency.equals("daily")) {
                addDetails(newSchedule, commonDescription, startDate, endDate, event,false);
            }
            if (frequency.equals("biweekly")) {
                addDetails(newSchedule, commonDescription, startDate, endDate, event,true);
            }
        }
    }

    private void addDetails(FSchedule schedule,
                            String detailDescription,
                            LocalDate startDate,
                            LocalDate endDate,
                            FixDetailAddDto event,
                            boolean isBiweekly
    ) {

        LocalDate currentDate = startDate;
        List<String> days = event.getDays();
        LocalTime duration = event.getDuration();
        LocalTime startTime = event.getStartTime();

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            for (String day : days) {
                DayOfWeek dayOfWeek = ScheduleDay.of(day);
                if (currentDayOfWeek.equals(dayOfWeek)) {
                    LocalDateTime startDateTime = LocalDateTime.of(currentDate, startTime);
                    LocalDateTime endDateTime = startDateTime.plusHours(duration.getHour())
                            .plusMinutes(duration.getMinute());
                    FScheduleDetail fscheduleDetail = FScheduleDetail.createFscheduleDetail(detailDescription,
                            startDateTime, endDateTime);
                    fscheduleDetail.setFSchedule(schedule);
                }
            }
            if(isBiweekly && currentDayOfWeek.equals(DayOfWeek.SUNDAY)){
                currentDate = currentDate.plusDays(7);
            }
            currentDate = currentDate.plusDays(1);
        }
    }
}
