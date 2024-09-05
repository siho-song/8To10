package show.schedulemanagement.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.vSchedule.VSchedule;
import show.schedulemanagement.dto.schedule.request.vSchedule.VScheduleUpdate;

@Service
@RequiredArgsConstructor
public class VScheduleService {

    private final ScheduleService scheduleService;

    @Transactional
    public void update(Member member, VScheduleUpdate vScheduleUpdate){

        VSchedule schedule = (VSchedule) scheduleService.findById(vScheduleUpdate.getId());
        if(!schedule.getCreatedBy().equals(member.getEmail())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        schedule.update(vScheduleUpdate);
    }
}
