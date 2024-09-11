package show.schedulemanagement.service.schedule.fschedule;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fschedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleDetailUpdate;
import show.schedulemanagement.repository.schedule.fschedule.FScheduleDetailRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FScheduleDetailService {
    private final FScheduleDetailRepository fScheduleDetailRepository;

    public FScheduleDetail findById(Long id) {
        //TODO 예외처리
        return fScheduleDetailRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 고정일정 세부사항을 찾을 수 없습니다."));
    }

    public List<FScheduleDetail> findByStartDateGEAndEmailAndParentId(LocalDateTime start, String email, Long parentId){
        return fScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(start, email , parentId);
    }

    @Transactional
    public int deleteByFScheduleDetails(List<FScheduleDetail> fScheduleDetails){
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
    public void update(Member member, FScheduleDetailUpdate fScheduleDetailUpdate){
        Long id = fScheduleDetailUpdate.getId();
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자가 일치하지 않습니다.");
        }
        fScheduleDetail.update(fScheduleDetailUpdate);
    }

    @Transactional
    public void deleteByStartDateGEAndMemberAndParentId(LocalDateTime start, Member member, Long parentId) {
        List<FScheduleDetail> fScheduleDetails = findByStartDateGEAndEmailAndParentId(start, member.getEmail(), parentId);
        deleteByFScheduleDetails(fScheduleDetails);
    }
}
