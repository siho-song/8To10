package show.schedulemanagement.service.schedule.timeslot;

import static show.schedulemanagement.constant.AppConstant.WORK_END_TIME;
import static show.schedulemanagement.constant.AppConstant.WORK_START_TIME;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.schedule.ScheduleAble;

@Service
@Slf4j
public class TimeSlotService {

    public Map<LocalDate, List<TimeSlot>> findAllForPeriodByDate(
            Map<LocalDate, List<ScheduleAble>> scheduleMap,
            LocalDate startDate,
            LocalDate endDate) {

        Map<LocalDate, List<TimeSlot>> slotsByDate = new HashMap<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            List<ScheduleAble> scheduleAbles = scheduleMap.getOrDefault(currentDate, Collections.emptyList());
            List<TimeSlot> timeslots = findSlotsForDate(scheduleAbles, currentDate);
            slotsByDate.put(currentDate, timeslots);
            currentDate = currentDate.plusDays(1);
        }

        return slotsByDate;
    }

    private List<TimeSlot> findSlotsForDate(List<ScheduleAble> scheduleAbles, LocalDate currentDate) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        if (scheduleAbles.isEmpty()) {
            return addTotalWorkTime();
        }

        LocalDateTime dayStart = currentDate.atTime(WORK_START_TIME);
        LocalDateTime dayEnd = currentDate.atTime(WORK_END_TIME);

        // 일과 시작시간과 첫번째 스케쥴의 시작시간 사이시간을 확인하고, 0보다 클 경우 timeslot에 추가한다.
        LocalDateTime firstScheduleStart = scheduleAbles.get(0).getStartDate();
        if (canSlot(dayStart, firstScheduleStart)) {
            timeSlots.add(new TimeSlot(dayStart.toLocalTime(), firstScheduleStart.toLocalTime()));
        }

        // 스케쥴끼리 비교하고 사이에 비는시간이 0보다 클 경우 timeslot에 추가한다.
        LocalDateTime lastEndTime = null;
        for (ScheduleAble scheduleAble : scheduleAbles) {
            LocalDateTime startTime = scheduleAble.getStartDate();
            LocalDateTime endTime = scheduleAble.getEndDate();

            if (lastEndTime != null && canSlot(lastEndTime, startTime)) {
                timeSlots.add(new TimeSlot(lastEndTime.toLocalTime(), startTime.toLocalTime()));
            }
            lastEndTime = endTime;
        }

        // 마지막 일정과 일과 종료시간의 사이시간을 확인하고, necassary 만큼의 여유가 있으면 timeslot에 추가한다.
        LocalDateTime lastScheduleEnd = scheduleAbles.get(scheduleAbles.size() - 1).getEndDate();
        if (canSlot(lastScheduleEnd, dayEnd)) {
            timeSlots.add(new TimeSlot(lastScheduleEnd.toLocalTime(), dayEnd.toLocalTime()));
        }

        return timeSlots;
    }

    private boolean canSlot(LocalDateTime dayStart, LocalDateTime firstScheduleStart) {
        return Duration.between(dayStart, firstScheduleStart).toMinutes() > 0;
    }

    private List<TimeSlot> addTotalWorkTime() {
        return List.of(new TimeSlot(WORK_START_TIME, WORK_END_TIME));
    }

    public Map<DayOfWeek, List<TimeSlot>> findCommonRangeSlotsByDay(Map<LocalDate, List<TimeSlot>> allSlotsForPeriodByDate, Duration necessaryTime) {
        Map<DayOfWeek, List<TimeSlot>> commonRangeSlotsByDay = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {

            if(!hasAllDateSlots(allSlotsForPeriodByDate, dayOfWeek)){
                continue;
            }

            List<List<TimeSlot>> allSlotsForDays = getSlotsByDay(allSlotsForPeriodByDate, dayOfWeek)
                    .values().stream()
                    .toList();

            if(allSlotsForDays.isEmpty()){
                continue;
            }

            List<TimeSlot> commonTimeSlots = findCommonTimeSlots(necessaryTime, allSlotsForDays);

            if(!commonTimeSlots.isEmpty()) {
                commonRangeSlotsByDay.put(dayOfWeek, commonTimeSlots);
            }
        }
        return commonRangeSlotsByDay;
    }

    private boolean hasAllDateSlots(Map<LocalDate, List<TimeSlot>> allSlotsForPeriodByDate, DayOfWeek dayOfWeek) {
        return allSlotsForPeriodByDate.entrySet().stream()
                .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                .noneMatch(entry -> entry.getValue().isEmpty());
    }

    private List<TimeSlot> findCommonTimeSlots(Duration necessaryTime, List<List<TimeSlot>> allSlotsForDays) {
        List<TimeSlot> finalList = new ArrayList<>(allSlotsForDays.get(0));
        for (int i = 1; i < allSlotsForDays.size() ; i++) {
            List<TimeSlot> nextDaySlots = allSlotsForDays.get(i);
            List<TimeSlot> tempSlots = new ArrayList<>();
            for (TimeSlot slot1 : finalList) {
                for (TimeSlot slot2 : nextDaySlots) {
                    LocalTime start = max(slot1.getStartTime(), slot2.getStartTime());
                    LocalTime end = min(slot1.getEndTime(), slot2.getEndTime());

                    Duration duration = Duration.between(start, end);

                    if (!start.isAfter(end) && duration.compareTo(necessaryTime) >= 0) { // 겹치는 부분이 있는지 확인
                        tempSlots.add(new TimeSlot(start, end));
                    }
                }
            }
            finalList = tempSlots;
            if(finalList.isEmpty()) break;
        }
        return finalList;
    }

    private LinkedHashMap<LocalDate, List<TimeSlot>> getSlotsByDay(Map<LocalDate, List<TimeSlot>> allSlotsForPeriodByDate,
                                                                DayOfWeek dayOfWeek) {
        return allSlotsForPeriodByDate.entrySet().stream()
                .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (existValue, newValue) -> existValue,
                        LinkedHashMap::new
                ));
    }

    private LocalTime max(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2) ? time1 : time2;
    }

    private LocalTime min(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2) ? time1 : time2;
    }
}