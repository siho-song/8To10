package show.schedulemanagement.service.schedule.fschedule;

import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_F_DETAIL;
import static show.schedulemanagement.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fschedule.FScheduleDetail;
import show.schedulemanagement.dto.schedule.request.fschedule.FScheduleDetailUpdate;
import show.schedulemanagement.exception.MismatchException;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.schedule.fschedule.FScheduleDetailRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FScheduleDetailService {
    private final FScheduleDetailRepository fScheduleDetailRepository;

    public FScheduleDetail findById(Long id) {
        return fScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_DETAIL));
    }

    public List<FScheduleDetail> findByStartDateGEAndEmailAndParentId(
            LocalDateTime start,
            String email,
            Long parentId)
    {
        return fScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(start, email , parentId);
    }

    @Transactional
    public void deleteByFScheduleDetails(List<FScheduleDetail> fScheduleDetails){
        fScheduleDetailRepository.deleteByFScheduleDetails(fScheduleDetails);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fScheduleDetailRepository.delete(fScheduleDetail);
    }

    @Transactional
    public void update(Member member, FScheduleDetailUpdate fScheduleDetailUpdate){
        Long id = fScheduleDetailUpdate.getId();
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fScheduleDetail.update(fScheduleDetailUpdate);
    }

    @Transactional
    public void deleteByStartDateGEAndMemberAndParentId(
            LocalDateTime start,
            Member member,
            Long parentId)
    {
        List<FScheduleDetail> fScheduleDetails = findByStartDateGEAndEmailAndParentId(start, member.getEmail(), parentId);
        deleteByFScheduleDetails(fScheduleDetails);
    }
}
