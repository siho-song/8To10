package show.schedulemanagement.service.schedule.vschedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.vschedule.VSchedule;
import show.schedulemanagement.dto.schedule.request.vschedule.VScheduleUpdate;
import show.schedulemanagement.service.schedule.ScheduleService;

@Service
@RequiredArgsConstructor
public class VScheduleService {

    private final ScheduleService scheduleService;

    @Transactional
    public void update(Member member, VScheduleUpdate vScheduleUpdate){

        VSchedule schedule = (VSchedule) scheduleService.findById(vScheduleUpdate.getId());
        if(!member.isSameEmail(schedule.getCreatedBy())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        schedule.update(vScheduleUpdate);
    }
}
