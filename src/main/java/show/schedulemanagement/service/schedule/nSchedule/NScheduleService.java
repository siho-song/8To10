package show.schedulemanagement.service.schedule.nSchedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.nSchedule.NScheduleSave;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.timeslot.TimeSlot;
import show.schedulemanagement.service.schedule.timeslot.TimeSlotService;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NScheduleService {

    private final ScheduleService scheduleService;
    private final TimeSlotService timeSlotService;
    private final Random random = new Random();

    @Transactional
    public NSchedule addNSchedule(Member member, NScheduleSave dto) {
        int performInWeek = dto.getPerformInWeek();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        List<Schedule> schedules = scheduleService.findAllBetweenStartAndEnd(
                member,
                startDate,
                endDate
        );

        List<ScheduleAble> scheduleAbles = scheduleService.getAllScheduleAbles(schedules);

        Map<LocalDate, List<TimeSlot>> slotMapForEachDate = timeSlotService.findAllBetweenStartAndEnd(
                getSortedScheduleAbleMapForEachDate(scheduleAbles),
                startDate,
                endDate
        );

        Map<DayOfWeek, List<TimeSlot>> availableSlotMap = timeSlotService.findCommonSlotsForEachDay(
                slotMapForEachDate,
                dto.getNecessaryTime()
        );

        List<DayOfWeek> selectedDays = selectRandomDays(
                dto.getDays(),
                availableSlotMap.keySet(),
                performInWeek
        );

        validateCreateNSchedule(selectedDays, performInWeek);

        Map<DayOfWeek, TimeSlot> selectedTimeSlots = timeSlotService.selectRandomTimeSlots(
                selectedDays,
                availableSlotMap
        );

        NSchedule nSchedule = NSchedule.createNSchedule(member, dto);
        addNScheduleDetails(
                nSchedule,
                selectedTimeSlots,
                dto.getPerformInDay(),
                nSchedule.getBufferTime()
        );

        return nSchedule;
    }

    private Map<LocalDate, List<ScheduleAble>> getSortedScheduleAbleMapForEachDate(List<ScheduleAble> scheduleAbles) {
        return scheduleAbles.stream()
                .collect(Collectors.groupingBy(scheduleAble -> scheduleAble.getStartDate().toLocalDate(),
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            list.sort(Comparator.comparing(ScheduleAble::getStartDate));
                            return list;
                        })));
    }

    private List<DayOfWeek> selectRandomDays(List<DayOfWeek> candidateDays, Set<DayOfWeek> availableDays, int numberOfDays) {
        List<DayOfWeek> selectableDays = candidateDays.stream()
                .filter(availableDays::contains)
                .collect(Collectors.toList());
        Collections.shuffle(selectableDays, random);
        return selectableDays.subList(0, Math.min(numberOfDays, selectableDays.size()));
    }

    private void validateCreateNSchedule(List<DayOfWeek> selectedDays, int performInWeek) {
        if(selectedDays.size() < performInWeek){
            String errorMessage = String.format(
                    "일반 일정을 생성할 수 없습니다. 일정을 조율해주세요. %d 만큼의 수행일 수를 줄이거나, %d 만큼의 수행일 수를 늘려야 합니다.",
                    performInWeek - selectedDays.size()
            );
            throw new RuntimeException(errorMessage);
        }
    }

    private void addNScheduleDetails(NSchedule nSchedule, Map<DayOfWeek, TimeSlot> availableTimeSlots, LocalTime performInDay, LocalTime bufferTime) {
        LocalDateTime current = nSchedule.getStartDate().toLocalDate().atStartOfDay();
        LocalDateTime end = nSchedule.getEndDate().toLocalDate().atStartOfDay();

        while(!current.isAfter(end)){
            LocalDateTime startDate = current;
            DayOfWeek day = current.getDayOfWeek();
            TimeSlot timeSlot = availableTimeSlots.get(day);
            if(timeSlot != null){
                LocalDateTime startDateTime = startDate.plusHours(timeSlot.getStartTime().getHour()+bufferTime.getHour())
                        .plusMinutes(timeSlot.getStartTime().getMinute()+bufferTime.getMinute());

                LocalDateTime endDateTime = startDateTime.plusHours(performInDay.getHour())
                        .plusMinutes(performInDay.getMinute());

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
            if (i == size - 1){
                if(!Double.isNaN((roundedDiv + difference / roundedDiv))){
                    nScheduleDetails.get(i).setDailyAmount(roundedDiv + difference / roundedDiv);
                }
                else {
                    nScheduleDetails.get(i).setDailyAmount(roundedDiv);
                }
            } else {
                nScheduleDetails.get(i).setDailyAmount(roundedDiv);
            }
        }
    }
}