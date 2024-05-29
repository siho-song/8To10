package show.schedulemanagement.service.schedule.timeslot;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.schedule.ScheduleAble;

@Service
@Slf4j
public class TimeSlotService {

    public Map<LocalDate, List<TimeSlot>> findAvailableTimeSlotsForPeriod(
            Map<LocalDate, List<ScheduleAble>> scheduleMap,
            LocalTime startOfWork,
            LocalTime endOfWork,
            LocalDate startDate,
            LocalDate endDate,
            Duration necessaryTime) {

        // Validate input times
        if (startOfWork.isAfter(endOfWork)) {
            throw new IllegalArgumentException("Start of work time cannot be after End of work.");
        }

        Map<LocalDate, List<TimeSlot>> availableTimeSlotsByDate = new HashMap<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            List<ScheduleAble> scheduleAbles = scheduleMap.getOrDefault(currentDate, Collections.emptyList());
            List<TimeSlot> availableTimeSlots = findAvailableTimeSlotsForDay(scheduleAbles, necessaryTime, startOfWork, endOfWork);
            if (!availableTimeSlots.isEmpty()) {
                availableTimeSlotsByDate.put(currentDate, availableTimeSlots);
            }
            currentDate = currentDate.plusDays(1);
        }

        return availableTimeSlotsByDate;
    }

    private List<TimeSlot> findAvailableTimeSlotsForDay(List<ScheduleAble> scheduleAbles, Duration necessaryTime, LocalTime startOfWork, LocalTime endOfWork) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        boolean endOfWorkCrossesMidnight = endOfWork.isBefore(startOfWork);

        if (scheduleAbles.isEmpty()) {
            return calculateAvailableTimeSlots(startOfWork, endOfWork, necessaryTime);
        }

        LocalDateTime dayStart = scheduleAbles.get(0).getStartDate().toLocalDate().atTime(startOfWork);
        LocalDateTime dayEnd = scheduleAbles.get(0).getStartDate().toLocalDate().atTime(endOfWork);
        if (endOfWorkCrossesMidnight) {
            dayEnd = dayEnd.plusDays(1);
        }

        // Check for time before the first schedule
        LocalDateTime firstScheduleStart = scheduleAbles.get(0).getStartDate();
        if (Duration.between(dayStart, firstScheduleStart).compareTo(necessaryTime) >= 0) {
            timeSlots.add(new TimeSlot(dayStart.toLocalTime(), firstScheduleStart.toLocalTime()));
        }

        // Check between schedules
        LocalDateTime lastEndTime = null;
        for (ScheduleAble scheduleAble : scheduleAbles) {
            LocalDateTime startTime = scheduleAble.getStartDate();
            LocalDateTime endTime = scheduleAble.getEndDate();

            if (lastEndTime != null && Duration.between(lastEndTime, startTime).compareTo(necessaryTime) >= 0) {
                timeSlots.add(new TimeSlot(lastEndTime.toLocalTime(), startTime.toLocalTime()));
            }
            lastEndTime = endTime;
        }

        // Check for time after the last schedule
        LocalDateTime lastScheduleEnd = scheduleAbles.get(scheduleAbles.size() - 1).getEndDate();
        if (Duration.between(lastScheduleEnd, dayEnd).compareTo(necessaryTime) >= 0) {
            timeSlots.add(new TimeSlot(lastScheduleEnd.toLocalTime(), dayEnd.toLocalTime()));
        }

        return timeSlots;
    }

    private List<TimeSlot> calculateAvailableTimeSlots(LocalTime startOfWork, LocalTime endOfWork, Duration necessaryTime) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        if (endOfWork.isBefore(startOfWork)) {
            // Bed time crosses midnight
            LocalTime dayStart = LocalTime.MIDNIGHT;
            LocalTime dayEnd = LocalTime.MAX;

            if (Duration.between(startOfWork, dayEnd).compareTo(necessaryTime) >= 0) {
                timeSlots.add(new TimeSlot(startOfWork, dayEnd));
            }
            if (Duration.between(dayStart, endOfWork).compareTo(necessaryTime) >= 0) {
                timeSlots.add(new TimeSlot(dayStart, endOfWork));
            }
        } else {
            // Bed time is after wake up time within the same day
            if (Duration.between(startOfWork, endOfWork).compareTo(necessaryTime) >= 0) {
                timeSlots.add(new TimeSlot(startOfWork, endOfWork));
            }
        }

        return timeSlots;
    }

    public Map<DayOfWeek, List<TimeSlot>> findCommonTimeSlotsByDayOfWeek(Map<LocalDate, List<TimeSlot>> availableTimeSlotsForPeriod, Duration necessaryTime) {
        Map<DayOfWeek, List<TimeSlot>> commonTimeSlotsByDayOfWeek = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            List<List<TimeSlot>> timeSlotsByDay = availableTimeSlotsForPeriod.entrySet().stream()
                    .filter(entry -> entry.getKey().getDayOfWeek().equals(dayOfWeek))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            List<TimeSlot> commonTimeSlots = findCommonTimeSlots(timeSlotsByDay, necessaryTime);
            commonTimeSlotsByDayOfWeek.put(dayOfWeek, commonTimeSlots);
        }

        return commonTimeSlotsByDayOfWeek;
    }

    private List<TimeSlot> findCommonTimeSlots(List<List<TimeSlot>> timeSlotsByDay, Duration necessaryTime) {
        if (timeSlotsByDay.isEmpty()) {
            return Collections.emptyList();
        }

        List<TimeSlot> commonTimeSlots = new ArrayList<>(timeSlotsByDay.get(0));

        for (int i = 1; i < timeSlotsByDay.size(); i++) {
            commonTimeSlots = findOverlap(commonTimeSlots, timeSlotsByDay.get(i), necessaryTime);
        }

        return commonTimeSlots;
    }

    private List<TimeSlot> findOverlap(List<TimeSlot> slots1, List<TimeSlot> slots2, Duration necessaryTime) {
        List<TimeSlot> overlapSlots = new ArrayList<>();

        for (TimeSlot slot1 : slots1) {
            for (TimeSlot slot2 : slots2) {
                LocalTime overlapStart = slot1.getStartTime().isAfter(slot2.getStartTime()) ? slot1.getStartTime() : slot2.getStartTime();
                LocalTime overlapEnd = slot1.getEndTime().isBefore(slot2.getEndTime()) ? slot1.getEndTime() : slot2.getEndTime();

                if (overlapStart.isBefore(overlapEnd) && Duration.between(overlapStart, overlapEnd).compareTo(necessaryTime) >= 0) {
                    overlapSlots.add(new TimeSlot(overlapStart, overlapEnd));
                }
            }
        }

        return overlapSlots;
    }
}