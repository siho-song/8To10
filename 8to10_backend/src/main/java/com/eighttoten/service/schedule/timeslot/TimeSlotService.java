package com.eighttoten.service.schedule.timeslot;

import static com.eighttoten.constant.AppConstant.WORK_END_TIME;
import static com.eighttoten.constant.AppConstant.WORK_START_TIME;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
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
import com.eighttoten.domain.schedule.ScheduleAble;

@Service
public class TimeSlotService {
    public Map<LocalDate, List<TimeSlot>> findAllBetweenStartAndEnd(
            Map<LocalDate, List<ScheduleAble>> sortedScheduleAbleMap,
            LocalDate startDate,
            LocalDate endDate) {

        Map<LocalDate, List<TimeSlot>> slotsByDate = new HashMap<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            List<ScheduleAble> scheduleAbles = sortedScheduleAbleMap.getOrDefault(currentDate, Collections.emptyList());
            List<TimeSlot> timeslots = findSlotsForDate(scheduleAbles);
            slotsByDate.put(currentDate, timeslots);
            currentDate = currentDate.plusDays(1);
        }

        return slotsByDate;
    }

    public Map<DayOfWeek, List<TimeSlot>> findCommonSlotsForEachDay(
            Map<LocalDate, List<TimeSlot>> slotsForPeriodByDate,
            Duration necessaryTime) {

        Map<DayOfWeek, List<TimeSlot>> commonRangeSlotsByDay = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if(!hasAllDateSlots(slotsForPeriodByDate, dayOfWeek)){
                continue;
            }

            List<List<TimeSlot>> allSlotsForDateEqualDay = collectSlotsByDay(slotsForPeriodByDate, dayOfWeek)
                    .values()
                    .stream()
                    .toList();

            List<TimeSlot> commonTimeSlots = findCommonTimeSlots(necessaryTime, allSlotsForDateEqualDay);

            if(!commonTimeSlots.isEmpty()) {
                commonRangeSlotsByDay.put(dayOfWeek, commonTimeSlots);
            }
        }
        return commonRangeSlotsByDay;
    }

    public Map<DayOfWeek, TimeSlot> selectRandomTimeSlots(List<DayOfWeek> days, Map<DayOfWeek, List<TimeSlot>> availableTimeSlotsByDay) {
        return days.stream()
                .filter(availableTimeSlotsByDay::containsKey)
                .collect(Collectors.toMap(
                        day -> day,
                        day -> availableTimeSlotsByDay.get(day).get(0)
                ));
    }

    private List<TimeSlot> findSlotsForDate(List<ScheduleAble> sortedScheduleAbles) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        if (sortedScheduleAbles.isEmpty()) {
            return addTotalWorkTime();
        }

        LocalTime firstScheduleStart = sortedScheduleAbles.get(0).getStartDate().toLocalTime();
        if (isTimeSlotAvailable(WORK_START_TIME, firstScheduleStart)) {
            timeSlots.add(new TimeSlot(WORK_START_TIME, firstScheduleStart));
        }

        LocalTime beforeScheduleEnd = null;
        for (ScheduleAble scheduleAble : sortedScheduleAbles) {
            LocalTime nextScheduleStart = scheduleAble.getStartDate().toLocalTime();
            LocalTime nextScheduleEnd = scheduleAble.getEndDate().toLocalTime();

            if (beforeScheduleEnd != null && isTimeSlotAvailable(beforeScheduleEnd, nextScheduleStart)) {
                timeSlots.add(new TimeSlot(beforeScheduleEnd, nextScheduleStart));
            }
            beforeScheduleEnd = nextScheduleEnd;
        }

        LocalTime lastScheduleEnd = sortedScheduleAbles.get(sortedScheduleAbles.size() - 1).getEndDate().toLocalTime();
        if (isTimeSlotAvailable(lastScheduleEnd, WORK_END_TIME)) {
            timeSlots.add(new TimeSlot(lastScheduleEnd, WORK_END_TIME));
        }
        return timeSlots;
    }

    private List<TimeSlot> addTotalWorkTime() {
        return List.of(new TimeSlot(WORK_START_TIME, WORK_END_TIME));
    }

    private boolean isTimeSlotAvailable(LocalTime start, LocalTime end) {
        return Duration.between(start, end).toMinutes() > 0;
    }

    private boolean hasAllDateSlots(Map<LocalDate, List<TimeSlot>> allSlotsForPeriodByDate, DayOfWeek dayOfWeek) {
        return allSlotsForPeriodByDate.entrySet().stream()
                .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                .noneMatch(entry -> entry.getValue().isEmpty());
    }

    private LinkedHashMap<LocalDate, List<TimeSlot>> collectSlotsByDay(Map<LocalDate, List<TimeSlot>> slotsForPeriodByDate,
                                                                       DayOfWeek dayOfWeek) {
        return slotsForPeriodByDate.entrySet().stream()
                .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (existValue, newValue) -> existValue,
                        LinkedHashMap::new
                ));
    }

    private List<TimeSlot> findCommonTimeSlots(Duration necessaryTime, List<List<TimeSlot>> allSlotsForDays) {
        List<TimeSlot> finalList = new ArrayList<>(allSlotsForDays.get(0)); //최종리스트

        for (int i = 1; i < allSlotsForDays.size() ; i++) {
            List<TimeSlot> nextDaySlots = allSlotsForDays.get(i);
            List<TimeSlot> tempSlots = new ArrayList<>();
            for (TimeSlot slot1 : finalList) {
                for (TimeSlot slot2 : nextDaySlots) {
                    LocalTime start = getLaterTime(slot1.getStartTime(), slot2.getStartTime());
                    LocalTime end = getEarlierTime(slot1.getEndTime(), slot2.getEndTime());

                    Duration spareTime = Duration.between(start, end); //end-start

                    if (spareTime.compareTo(necessaryTime) >= 0) { // 겹치는 부분이 있는지 확인
                        tempSlots.add(new TimeSlot(start, end));
                    }
                }
            }
            finalList = tempSlots;
            if(finalList.isEmpty()) break;
        }
        return finalList;
    }

    private LocalTime getLaterTime(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2) ? time1 : time2;
    }

    private LocalTime getEarlierTime(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2) ? time1 : time2;
    }
}