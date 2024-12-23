package com.eighttoten.schedule.service;

import static com.eighttoten.global.exception.ExceptionCode.INVALID_N_SCHEDULE_CREATION;
import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_N_SCHEDULE;
import static com.eighttoten.global.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.global.constant.AppConstant;
import com.eighttoten.global.exception.BadRequestException;
import com.eighttoten.global.exception.MismatchException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.NSchedule;
import com.eighttoten.schedule.domain.NScheduleDetail;
import com.eighttoten.schedule.domain.Schedule;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.TimeSlot;
import com.eighttoten.schedule.domain.repository.NScheduleRepository;
import com.eighttoten.schedule.dto.request.NScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.NScheduleUpdateRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public NSchedule create(Member member, NScheduleSaveRequestRequest dto) {

        int performInWeek = dto.getPerformInWeek();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        List<Schedule> schedules = scheduleService
                .findAllBetweenStartAndEnd(member, startDate, endDate);

        List<ScheduleAble> scheduleAbles = scheduleService.getAllScheduleAbles(schedules);

        Map<LocalDate, List<TimeSlot>> slotMap = timeSlotService.findAllBetweenStartAndEnd(
                getSortedAfter8AMScheduleAbleMap(scheduleAbles), startDate, endDate
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

        NSchedule nSchedule = NSchedule.from(member, dto);
        addDetails(nSchedule, selectedTimeSlots, dto.getPerformInDay(), nSchedule.getBufferTime());

        return nSchedule;
    }

    @Transactional
    public void update(Member member, NScheduleUpdateRequest nScheduleUpdateRequest){

        NSchedule nSchedule = findById(nScheduleUpdateRequest.getId());
        if(!member.isSameEmail(nSchedule.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        nSchedule.update(nScheduleUpdateRequest);
    }

    private Map<LocalDate, List<ScheduleAble>> getSortedAfter8AMScheduleAbleMap(List<ScheduleAble> scheduleAbles) {
        LocalTime startTime = AppConstant.WORK_START_TIME;
        LocalTime endTime = AppConstant.WORK_END_TIME;

        return scheduleAbles.stream()
                .filter(scheduleAble -> isWithinTimeRange(scheduleAble, startTime, endTime))
                .collect(Collectors.groupingBy(
                        scheduleAble -> scheduleAble.getStartDate().toLocalDate(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    list.sort(Comparator.comparing(ScheduleAble::getStartDate));
                                    return list;
                                }
                        )
                ));
    }

    private boolean isWithinTimeRange(ScheduleAble scheduleAble, LocalTime startTime, LocalTime endTime) {
        LocalTime scheduleEnd = scheduleAble.getEndDate().toLocalTime();
        return !scheduleEnd.isBefore(startTime) && !scheduleEnd.isAfter(endTime);
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
            throw new BadRequestException(INVALID_N_SCHEDULE_CREATION);
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

                NScheduleDetail nscheduleDetail = NScheduleDetail.from(nSchedule.getCommonDescription(),
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