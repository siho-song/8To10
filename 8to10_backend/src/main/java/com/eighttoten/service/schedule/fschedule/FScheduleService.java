package com.eighttoten.service.schedule.fschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_F_SCHEDULE;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.ScheduleDay;
import com.eighttoten.domain.schedule.fschedule.FSchedule;
import com.eighttoten.domain.schedule.fschedule.FScheduleDetail;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleSave;
import com.eighttoten.dto.schedule.request.fschedule.FScheduleUpdate;
import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.schedule.fschedule.FScheduleRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FScheduleService {

    private final FScheduleRepository fScheduleRepository;

    public FSchedule findById(Long id){
        return fScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_SCHEDULE));
    }

    @Transactional
    public void addDetails(FSchedule schedule, FScheduleSave dto) {
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
    public void update(Member member, FScheduleUpdate fScheduleUpdate) {
        FSchedule fSchedule = findById(fScheduleUpdate.getId());
        if (!member.isSameEmail(fSchedule.getCreatedBy())) {
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fSchedule.update(fScheduleUpdate);
    }
}
