package show.schedulemanagement.service.schedule;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.ScheduleDay;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.NormalAddDto;
import show.schedulemanagement.dto.schedule.response.NormalResponseDto;
import show.schedulemanagement.dto.schedule.response.Result;
import show.schedulemanagement.service.schedule.timeslot.TimeSlot;
import show.schedulemanagement.service.schedule.timeslot.TimeSlotService;

@Service
@Slf4j
@RequiredArgsConstructor
public class NScheduleService {

    private final ScheduleService scheduleService;
    private final ScheduleFilterService scheduleFilterService;
    private final TimeSlotService timeSlotService;
    private final Random random = new Random();

    public NSchedule addNSchedule(Member member, NormalAddDto dto) {
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        List<DayOfWeek> candidateDays = dto.getCandidateDays();
        Duration necessaryTime = Duration.ofHours(dto.getNecessaryTime().getHour())
                .plusMinutes(dto.getNecessaryTime().getMinute());
        LocalTime wakeUpTime = member.getStartOfWork();
        LocalTime bedTime = member.getEndOfWork();
        int performInWeek = dto.getPerformInWeek();

        List<Schedule> allSchedule = scheduleService.findAllBetweenStartAndEnd(member, startDate, endDate);
        // ok
        Map<LocalDate, List<ScheduleAble>> sortedScheduleMap = new TreeMap<>(scheduleFilterService.filterScheduleByTime(allSchedule, wakeUpTime, bedTime));

        Map<LocalDate, List<TimeSlot>> availableTimeSlotsForPeriod = timeSlotService.findAvailableTimeSlotsForPeriod(
                sortedScheduleMap, wakeUpTime, bedTime, startDate, endDate, necessaryTime);

        Map<DayOfWeek, List<TimeSlot>> availableTimeSlots = timeSlotService.findCommonTimeSlotsByDayOfWeek(availableTimeSlotsForPeriod, necessaryTime);

        List<DayOfWeek> selectedDays = selectRandomDays(candidateDays, availableTimeSlots.keySet(), performInWeek);
        Map<DayOfWeek, TimeSlot> selectedTimeSlots = selectRandomTimeSlots(selectedDays, availableTimeSlots);

        canCreateNSchedule(selectedTimeSlots.size(), performInWeek);

        NSchedule nSchedule = NSchedule.createNSchedule(member, dto);
        addNScheduleDetails(nSchedule, selectedTimeSlots, necessaryTime,nSchedule.getBufferTime());

        log.debug("NScheduleService nSchedule : {} ", nSchedule);
        return nSchedule;
    }

    private void canCreateNSchedule(int size, int performInWeek) {
        if(size < performInWeek){
            String errorMessage = String.format("일반 일정을 생성할 수 없습니다. 일정을 조율해주세요. %d 만큼의 수행일 수를 줄여야 합니다.",
                    performInWeek - size);
            throw new RuntimeException(errorMessage);
        }
    }

    private void addNScheduleDetails(NSchedule nSchedule, Map<DayOfWeek, TimeSlot> timeSlots, Duration necessaryTime, LocalTime bufferTime) {
        LocalDateTime current = nSchedule.getStartDate().toLocalDate().atStartOfDay();
        LocalDateTime end = nSchedule.getEndDate().toLocalDate().atStartOfDay();

        while(!current.isAfter(end)){
            LocalDateTime startDate = current;
            DayOfWeek day = current.getDayOfWeek();
            TimeSlot timeSlot = timeSlots.get(day);
            if(timeSlot != null){
                LocalDateTime startDateTime = startDate.plusHours(timeSlot.getStartTime().getHour()+bufferTime.getHour())
                        .plusMinutes(timeSlot.getStartTime().getMinute()+bufferTime.getMinute());

                LocalDateTime endDateTime = startDateTime.plusHours(necessaryTime.toHours())
                        .plusMinutes(necessaryTime.toMinutesPart());

                NScheduleDetail nscheduleDetail = NScheduleDetail.createNscheduleDetail(nSchedule.getCommonDescription(),
                        startDateTime, endDateTime);
                nscheduleDetail.setNSchedule(nSchedule);
            }
            current = current.plusDays(1L);
        }
        setNScheduleDailyAmount(nSchedule.getNScheduleDetails(),nSchedule.getTotalAmount());
    }

    private void setNScheduleDailyAmount(List<NScheduleDetail> nScheduleDetails, Integer totalAmount) {
        int size = nScheduleDetails.size();
        double div = (double) totalAmount / size;
        log.debug("Original div : {} ", div);

        // 소수점 둘째 자리에서 반올림
        double roundedDiv = Math.round(div * 100.0) / 100.0;
        log.debug("Rounded div : {} ", roundedDiv);

        double totalRoundedAmount = roundedDiv * size;
        double difference = totalAmount - totalRoundedAmount;
        log.debug("Total rounded amount : {}, Difference : {}", totalRoundedAmount, difference);

        // 마지막 요소에 차이를 더하여 총량의 오차를 최소화
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                nScheduleDetails.get(i).setDailyAmount(roundedDiv + difference / roundedDiv);
            } else {
                nScheduleDetails.get(i).setDailyAmount(roundedDiv);
            }
        }
    }

    private List<DayOfWeek> selectRandomDays(List<DayOfWeek> candidateDays, Set<DayOfWeek> availableDays, int numberOfDays) {
        List<DayOfWeek> selectableDays = candidateDays.stream()
                .filter(availableDays::contains)
                .collect(Collectors.toList());
        Collections.shuffle(selectableDays, random);
        return selectableDays.subList(0, Math.min(numberOfDays, selectableDays.size()));
    }

    private Map<DayOfWeek, TimeSlot> selectRandomTimeSlots(List<DayOfWeek> selectedDays, Map<DayOfWeek, List<TimeSlot>> availableTimeSlotsByDay) {
        return selectedDays.stream()
                .filter(availableTimeSlotsByDay::containsKey)
                .collect(Collectors.toMap(
                        day -> day,
                        day -> availableTimeSlotsByDay.get(day).get(random.nextInt(availableTimeSlotsByDay.get(day).size()))
                ));
    }

    public Result<NormalResponseDto> getResult(NSchedule nSchedule) {
        Result<NormalResponseDto> result = new Result<>();
        List<NScheduleDetail> nScheduleDetails = nSchedule.getNScheduleDetails();
        List<NormalResponseDto> events = result.getEvents();
        for (NScheduleDetail nScheduleDetail : nScheduleDetails) {
            NormalResponseDto dto = new NormalResponseDto(nSchedule, nScheduleDetail);
            events.add(dto);
        }
        return result;
    }
}