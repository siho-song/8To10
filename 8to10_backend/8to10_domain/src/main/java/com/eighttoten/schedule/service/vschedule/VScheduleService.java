package com.eighttoten.schedule.service.vschedule;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.vschedule.NewVSchedule;
import com.eighttoten.schedule.domain.vschedule.VSchedule;
import com.eighttoten.schedule.domain.vschedule.VScheduleUpdate;
import com.eighttoten.schedule.domain.vschedule.repository.VScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VScheduleService {
    private final VScheduleRepository vScheduleRepository;

    @Transactional
    public void save(NewVSchedule newVSchedule) {
        vScheduleRepository.save(newVSchedule);
    }

    @Transactional
    public void update(Member member, VScheduleUpdate vScheduleUpdate){
        VSchedule vSchedule = vScheduleRepository.findById(vScheduleUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_V_SCHEDULE));

        member.checkIsSameEmail(vSchedule.getCreatedBy());
        vSchedule.update(vScheduleUpdate);
        vScheduleRepository.update(vSchedule);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        VSchedule vSchedule = vScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_V_SCHEDULE));

        member.checkIsSameEmail(vSchedule.getCreatedBy());
        vScheduleRepository.deleteById(id);
    }
}