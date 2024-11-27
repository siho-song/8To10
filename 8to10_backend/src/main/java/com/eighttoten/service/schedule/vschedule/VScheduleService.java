package com.eighttoten.service.schedule.vschedule;

import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.vschedule.VSchedule;
import com.eighttoten.dto.schedule.request.vschedule.VScheduleUpdate;
import com.eighttoten.exception.MismatchException;
import com.eighttoten.service.schedule.ScheduleService;

@Service
@RequiredArgsConstructor
public class VScheduleService {

    private final ScheduleService scheduleService;

    @Transactional
    public void update(Member member, VScheduleUpdate vScheduleUpdate){

        VSchedule schedule = (VSchedule) scheduleService.findById(vScheduleUpdate.getId());
        if(!member.isSameEmail(schedule.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        schedule.update(vScheduleUpdate);
    }
}
