package show.schedulemanagement.service.schedule.nschedule;

import jakarta.persistence.EntityNotFoundException;
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
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleSave;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleUpdate;
import show.schedulemanagement.repository.schedule.nschedule.NScheduleRepository;
import show.schedulemanagement.service.schedule.ScheduleService;
import show.schedulemanagement.service.schedule.timeslot.TimeSlot;
import show.schedulemanagement.service.schedule.timeslot.TimeSlotService;

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
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정 입니다."));
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

    public void update(Member member, NScheduleUpdate nScheduleUpdate){

        NSchedule nSchedule = findById(nScheduleUpdate.getId());
        if(!nSchedule.getCreatedBy().equals(member.getEmail())){
            throw new RuntimeException("작성자가 일치하지 않습니다.");
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
            String errorMessage = String.format(
                    "일반 일정을 생성할 수 없습니다. 일정을 조율해주세요. %d 만큼의 수행일 수를 줄이거나, %d 만큼의 수행일 수를 늘려야 합니다.",
                    performInWeek - selectedDays.size() , performInWeek - selectedDays.size()
            );
            throw new RuntimeException(errorMessage);
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
        setNScheduleDailyAmount(nSchedule.getNScheduleDetails(),nSchedule.getTotalAmount());
    }

    private void setNScheduleDailyAmount(
            List<NScheduleDetail> nScheduleDetails,
            Integer totalAmount) {

        int size = nScheduleDetails.size();
        double div = (double) totalAmount / size;

        double roundedDiv = Math.round(div * 100.0) / 100.0;

        double totalRoundedAmount = roundedDiv * size;
        double difference = totalAmount - totalRoundedAmount;

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