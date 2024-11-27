package com.eighttoten.service.schedule.nschedule;

import static com.eighttoten.exception.ExceptionCode.INVALID_N_SCHEDULE_CREATION;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_SCHEDULE;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

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
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.domain.schedule.nschedule.NSchedule;
import com.eighttoten.domain.schedule.nschedule.NScheduleDetail;
import com.eighttoten.dto.schedule.request.nschedule.NScheduleSave;
import com.eighttoten.dto.schedule.request.nschedule.NScheduleUpdate;
import com.eighttoten.exception.InvalidScheduleCreationException;
import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.schedule.nschedule.NScheduleRepository;
import com.eighttoten.service.schedule.ScheduleService;
import com.eighttoten.service.schedule.timeslot.TimeSlot;
import com.eighttoten.service.schedule.timeslot.TimeSlotService;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NScheduleService {

    private final ScheduleService scheduleService;
    private final NScheduleRepository nScheduleRepository;
    private final TimeSlotService timeSlotService;
    private final Random random = new Random();

    public NSchedule findById(Long id){
        return nScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));
    }

    @Transactional
    public NSchedule create(Member member, NScheduleSave dto) {

        int performInWeek = dto.getPerformInWeek();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        List<Schedule> schedules = scheduleService
                .findAllBetweenStartAndEnd(member, startDate, endDate);

        List<ScheduleAble> scheduleAbles = scheduleService.getAllScheduleAbles(schedules);

        Map<LocalDate, List<TimeSlot>> slotMap = timeSlotService.findAllBetweenStartAndEnd(
                getSortedScheduleAbleMap(scheduleAbles), startDate, endDate
        );

        Map<DayOfWeek, List<TimeSlot>> availableSlotMap = timeSlotService
                .findCommonSlotsForEachDay(slotMap, dto.getNecessaryTime());

        List<DayOfWeek> selectedDays = selectRandomDays(
                dto.getDays(),
                availableSlotMap.keySet(),
                performInWeek
        );

        validateCreateNSchedule(selectedDays, performInWeek);

        Map<DayOfWeek, TimeSlot> selectedTimeSlots = timeSlotService
                .selectRandomTimeSlots(selectedDays, availableSlotMap);

        NSchedule nSchedule = NSchedule.createNSchedule(member, dto);
        addDetails(nSchedule, selectedTimeSlots, dto.getPerformInDay(), nSchedule.getBufferTime());

        return nSchedule;
    }

    @Transactional
    public void update(Member member, NScheduleUpdate nScheduleUpdate){

        NSchedule nSchedule = findById(nScheduleUpdate.getId());
        if(!member.isSameEmail(nSchedule.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        nSchedule.update(nScheduleUpdate);
    }

    private Map<LocalDate, List<ScheduleAble>> getSortedScheduleAbleMap(List<ScheduleAble> scheduleAbles) {

        return scheduleAbles.stream()
                .collect(Collectors.groupingBy(scheduleAble -> scheduleAble.getStartDate().toLocalDate(),
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            list.sort(Comparator.comparing(ScheduleAble::getStartDate));
                            return list;
                        })));
    }

    private List<DayOfWeek> selectRandomDays(
            List<DayOfWeek> candidateDays,
            Set<DayOfWeek> availableDays,
            int numberOfDays) {

        List<DayOfWeek> selectableDays = candidateDays.stream()
                .filter(availableDays::contains)
                .collect(Collectors.toList());
        Collections.shuffle(selectableDays, random);
        return selectableDays.subList(0, Math.min(numberOfDays, selectableDays.size()));
    }

    private void validateCreateNSchedule(List<DayOfWeek> selectedDays, int performInWeek) {
        if(selectedDays.size() < performInWeek){
            throw new InvalidScheduleCreationException(INVALID_N_SCHEDULE_CREATION);
        }
    }

    private void addDetails(
            NSchedule nSchedule,
            Map<DayOfWeek, TimeSlot> availableTimeSlots,
            LocalTime performInDay,
            LocalTime bufferTime) {

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
        setScheduleDailyAmount(nSchedule.getNScheduleDetails(),nSchedule.getTotalAmount());
    }

    private void setScheduleDailyAmount(
            List<NScheduleDetail> nScheduleDetails,
            int totalAmount) {

        int size = nScheduleDetails.size();
        int div = totalAmount / size;
        int mod = totalAmount % size;

        for (int i = 0; i < size; i++) {
            if (i < mod){
                nScheduleDetails.get(i).setDailyAmount(div + 1);
            } else {
                nScheduleDetails.get(i).setDailyAmount(div);
            }
        }
    }
}