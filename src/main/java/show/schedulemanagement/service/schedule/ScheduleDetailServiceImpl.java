package show.schedulemanagement.service.schedule;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.repository.schedule.detail.FScheduleDetailRepository;
import show.schedulemanagement.repository.schedule.detail.NScheduleDetailRepository;
import show.schedulemanagement.service.MemberService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ScheduleDetailServiceImpl implements ScheduleDetailService{

    private final ScheduleService scheduleService;
    private final FScheduleDetailRepository fScheduleDetailRepository;
    private final NScheduleDetailRepository nScheduleDetailRepository;

    @Override
    @Transactional
    public void deleteFdById(Member member, Long id) {
        FScheduleDetail fScheduleDetail = findFdById(id);
        FSchedule fSchedule = fScheduleDetail.getFSchedule();
        List<FScheduleDetail> fScheduleDetails = fSchedule.getFScheduleDetails();

        if(member.getEmail().equals(fSchedule.getCreatedBy())){
            fScheduleDetails.remove(fScheduleDetail);
        }

        if(fScheduleDetails.isEmpty()){
            scheduleService.deleteById(member, fSchedule.getId());
        }
    }

    @Override
    @Transactional
    public void deleteNdById(Member member, Long id) {
        NScheduleDetail nScheduleDetail = findNdById(id);
        NSchedule nSchedule = nScheduleDetail.getNSchedule();
        List<NScheduleDetail> nScheduleDetails = nSchedule.getNScheduleDetails();
        if (member.getEmail().equals(nSchedule.getCreatedBy())) {
            nScheduleDetails.remove(nScheduleDetail);
            nSchedule.minusUpdateTotalAmount(nScheduleDetail.getDailyAmount().intValue());
        }

        if (nScheduleDetails.isEmpty()) {
            scheduleService.deleteById(member, nSchedule.getId());
        }
    }

    @Override
    public FScheduleDetail findFdById(Long id) {
        //TODO 예외처리
        return fScheduleDetailRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 고정일정 세부사항을 찾을 수 없습니다."));
    }

    @Override
    public NScheduleDetail findNdById(Long id) {
        return nScheduleDetailRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 일반일정 세부사항을 찾을 수 없습니다."));
    }
}
