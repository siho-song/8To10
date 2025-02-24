package com.eighttoten.schedule.service;

import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_SCHEDULE;
import static com.eighttoten.global.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.global.exception.MismatchException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.Schedule;
import com.eighttoten.schedule.domain.ScheduleAble;
import com.eighttoten.schedule.domain.repository.ScheduleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_SCHEDULE));
    }

    public List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end) {
        return scheduleRepository.findAllBetweenStartAndEnd(
                member,
                LocalDateTime.of(start, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(23, 59)));
    }

    public List<Schedule> findAllWithDetailByMember(Member member) {
        return scheduleRepository.findAllWithDetailByMember(member);
    }

    public List<ScheduleAble> getAllScheduleAbles(List<Schedule> schedules) {
        return schedules.stream().flatMap(schedule -> schedule.getScheduleAbles().stream()).toList();
    }

    @Transactional
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteById(Member member, Long id){
        Schedule schedule = findById(id);
        String createdBy = schedule.getCreatedBy();
        if(member.isSameEmail(createdBy)) {
            scheduleRepository.deleteById(id);
            return;
        }
        throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
    }
}