package com.eighttoten.schedule.service;

import static com.eighttoten.global.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.global.exception.MismatchException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.VSchedule;
import com.eighttoten.schedule.dto.request.VScheduleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VScheduleService {
    private final ScheduleService scheduleService;

    @Transactional
    public void update(Member member, VScheduleUpdateRequest vScheduleUpdateRequest){

        VSchedule schedule = (VSchedule) scheduleService.findById(vScheduleUpdateRequest.getId());
        if(!member.isSameEmail(schedule.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        schedule.update(vScheduleUpdateRequest);
    }
}