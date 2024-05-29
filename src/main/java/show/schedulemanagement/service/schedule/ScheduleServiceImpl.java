package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.repository.schedule.ScheduleRepository;
import show.schedulemanagement.service.MemberService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws Exception {
        Member member = memberService.getAuthenticatedMember();
        Schedule schedule = findById(id);
        String createdBy = schedule.getCreatedBy();
        if(member.getEmail().equals(createdBy)){
            scheduleRepository.deleteById(id);
        }
        throw new Exception("작성자만 삭제할 수 있습니다.");
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule Not Found"));
    }

    @Override
    public Schedule getConflictSchedule(List<ScheduleAble> newSchedule, List<Schedule> allSchedule) {
        for (Schedule schedule : allSchedule) {
            for (ScheduleAble scheduleAble : newSchedule) {
                if(schedule.isConflict(scheduleAble)){
                    return schedule;
                }
            }
        }
        return null;
    }

    @Override
    public List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end) {
        return scheduleRepository.findAllBetweenStartAndEnd(
                member,
                LocalDateTime.of(start, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(0, 0)));
    }
}
