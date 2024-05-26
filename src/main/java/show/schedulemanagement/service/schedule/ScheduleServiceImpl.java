package show.schedulemanagement.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.repository.schedule.ScheduleRepository;

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
        //파라미터로 부터 -> 일반일정 세부사항 여러개를 만들고 -> 이 각 세부사항의 범위
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule Not Found"));
    }
}
