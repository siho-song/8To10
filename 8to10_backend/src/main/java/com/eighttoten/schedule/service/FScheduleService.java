package com.eighttoten.schedule.service;

import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_F_SCHEDULE;
import static com.eighttoten.global.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.global.exception.MismatchException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.FSchedule;
import com.eighttoten.schedule.domain.FScheduleDetail;
import com.eighttoten.schedule.domain.ScheduleDay;
import com.eighttoten.schedule.domain.repository.FScheduleRepository;
import com.eighttoten.schedule.dto.request.FScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.FScheduleUpdateRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FScheduleService {
    private final FScheduleRepository fScheduleRepository;

    public FSchedule findById(Long id){
        return fScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_SCHEDULE));
    }

    @Transactional
    public void addDetails(FSchedule schedule, FScheduleSaveRequestRequest dto) {
        LocalDate currentDate = schedule.getStartDate().toLocalDate();
        LocalDate endDate = schedule.getEndDate().toLocalDate();
        List<String> days = dto.getDays();
        LocalTime duration = dto.getDuration();
        LocalTime startTime = dto.getStartTime();

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

    @Transactional
    public void update(Member member, FScheduleUpdateRequest fScheduleUpdateRequest) {
        FSchedule fSchedule = findById(fScheduleUpdateRequest.getId());
        if (!member.isSameEmail(fSchedule.getCreatedBy())) {
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fSchedule.update(fScheduleUpdateRequest);
    }
}