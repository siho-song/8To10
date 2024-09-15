package show.schedulemanagement.service.schedule.nschedule;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NSchedule;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import show.schedulemanagement.dto.schedule.request.nschedule.ToDoUpdate;
import show.schedulemanagement.repository.schedule.nschedule.NScheduleDetailRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NScheduleDetailService {

    private final NScheduleDetailRepository nScheduleDetailRepository;
    private final NScheduleService nScheduleService;

    public NScheduleDetail findById(Long id){
        return nScheduleDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));
    }

    public NScheduleDetail findByIdWithParent(Long id){
        return nScheduleDetailRepository.findByIdWithParent(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 일정이 존재하지 않습니다."));
    }

    @Transactional
    public void update(Member member, NScheduleDetailUpdate nScheduleDetailUpdate){
        NScheduleDetail nScheduleDetail = findById(nScheduleDetailUpdate.getId());
        if(!member.getEmail().equals(nScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자가 일치하지 않습니다.");
        }
        nScheduleDetail.update(nScheduleDetailUpdate);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        NScheduleDetail nScheduleDetail = findByIdWithParent(id);
        if(!member.getEmail().equals(nScheduleDetail.getCreatedBy())){
            throw new RuntimeException("작성자가 일치하지 않습니다.");
        }

        NSchedule parent = nScheduleDetail.getNSchedule();
        parent.updateTotalAmount(true, nScheduleDetail.getDailyAmount());

        nScheduleDetailRepository.delete(nScheduleDetail);
    }

    @Transactional
    public void deleteByStartDateGEAndMemberAndParentId(
            LocalDateTime startDate,
            Member member,
            Long parentId)
    {
        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(
                startDate,
                member.getEmail(),
                parentId);

        NSchedule parent = nScheduleService.findById(parentId);
        parent.updateTotalAmount(true, getDailyAmountSum(nScheduleDetails));
        nScheduleDetailRepository.deleteByNScheduleDetails(nScheduleDetails);
    }

    @Transactional
    public void updateCompleteStatuses(Member member, List<ToDoUpdate> toDoUpdates){
        List<Long> ids = toDoUpdates.stream().map(ToDoUpdate::getId).toList();
        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByIds(ids);

        nScheduleDetails.stream().filter(nScheduleDetail -> nScheduleDetail.isWriter(member.getEmail()))
                .forEach(nScheduleDetail -> toDoUpdates
                        .stream()
                        .filter(toDoUpdate -> toDoUpdate.isSameId(nScheduleDetail.getId()))
                        .findFirst()
                        .ifPresent(toDoUpdate -> nScheduleDetail.updateCompleteStatus(
                                toDoUpdate.isComplete())));
    }

    private double getDailyAmountSum(List<NScheduleDetail> nScheduleDetails) {
        return nScheduleDetails.stream().mapToDouble(NScheduleDetail::getDailyAmount).sum();
    }
}
