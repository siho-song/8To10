package show.schedulemanagement.service.schedule;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.repository.schedule.detail.FScheduleDetailRepository;
import show.schedulemanagement.service.MemberService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ScheduleDetailServiceImpl implements ScheduleDetailService{
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final FScheduleDetailRepository fScheduleDetailRepository;

    @Override
    @Transactional
    public void deleteFdById(Long id) {
        Member member = memberService.getAuthenticatedMember();
        FScheduleDetail fScheduleDetail = findFdById(id);
        FSchedule fSchedule = fScheduleDetail.getFSchedule();
        List<FScheduleDetail> fScheduleDetails = fSchedule.getFScheduleDetails();

        if(member.getEmail().equals(fSchedule.getCreatedBy())){
            fScheduleDetails.remove(fScheduleDetail);
        }

        if(fScheduleDetails.isEmpty()){
            scheduleService.deleteById(fSchedule.getId());
        }
    }

    @Override
    public FScheduleDetail findFdById(Long id) {
        //TODO 예외처리
        return fScheduleDetailRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("해당 고정일정 세부사항을 찾을 수 없습니다."));
    }
}
