package show.schedulemanagement.service.schedule.fSchedule;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.repository.schedule.detail.FScheduleDetailRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FScheduleDetailService {
    private final FScheduleDetailRepository fScheduleDetailRepository;

    public FScheduleDetail findById(Long id) {
        //TODO 예외처리
        return fScheduleDetailRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 고정일정 세부사항을 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        fScheduleDetailRepository.delete(fScheduleDetail);
    }
}
