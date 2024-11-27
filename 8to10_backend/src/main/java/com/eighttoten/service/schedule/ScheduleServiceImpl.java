package com.eighttoten.service.schedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_SCHEDULE;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.schedule.ScheduleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
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

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_SCHEDULE));
    }

    @Override
    public List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end) {
        return scheduleRepository.findAllBetweenStartAndEnd(
                member,
                LocalDateTime.of(start, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(0, 0)));
    }

    @Override
    public List<Schedule> findAllWithDetailByMember(Member member) {
        return scheduleRepository.findAllWithDetailByMember(member);
    }

    @Override
    public List<ScheduleAble> getAllScheduleAbles(List<Schedule> schedules) {
        return schedules.stream().flatMap(schedule -> schedule.getScheduleAbles().stream()).toList();
    }
}
