package show.schedulemanagement.service.schedule.fSchedule;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.repository.schedule.fSchedule.FScheduleDetailRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FScheduleDetailService {
    private final FScheduleDetailRepository fScheduleDetailRepository;

    public FScheduleDetail findById(Long id) {
        //TODO 예외처리
        return fScheduleDetailRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 고정일정 세부사항을 찾을 수 없습니다."));
    }

    public List<FScheduleDetail> findByStartDateGEAndEmailAndParent(LocalDateTime start, String email, FSchedule parent){
        return fScheduleDetailRepository.findByStartDateGEAndEmailAndParent(start, email , parent);
    }

    int deleteByFScheduleDetails(List<FScheduleDetail> fScheduleDetails){
        return fScheduleDetailRepository.deleteByFScheduleDetails(fScheduleDetails);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        fScheduleDetailRepository.delete(fScheduleDetail);
    }

    @Transactional
    public int deleteByStartDateGEAndMemberAndParent(LocalDateTime start, Member member, FSchedule fSchedule) {
        List<FScheduleDetail> fScheduleDetails = findByStartDateGEAndEmailAndParent(start, member.getEmail(), fSchedule);
        return deleteByFScheduleDetails(fScheduleDetails);
    }
}
