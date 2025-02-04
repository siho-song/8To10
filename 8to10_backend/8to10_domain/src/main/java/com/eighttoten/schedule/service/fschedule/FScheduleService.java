package com.eighttoten.schedule.service.fschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_F_SCHEDULE;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.FScheduleUpdate;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FScheduleService {
    private final FScheduleRepository fScheduleRepository;

    @Transactional(readOnly = true)
    public FSchedule findById(Long id){
        return fScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_SCHEDULE));
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        FSchedule fSchedule = fScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_SCHEDULE));

        if (!member.isSameEmail(fSchedule.getCreatedBy())) {
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fScheduleRepository.deleteById(id);
    }

    @Transactional
    public long save(NewFSchedule newFSchedule) {
        return fScheduleRepository.save(newFSchedule);
    }

    @Transactional
    public void update(Member member, FScheduleUpdate fScheduleUpdate) {
        FSchedule fSchedule = fScheduleRepository.findById(fScheduleUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_SCHEDULE));

        if (!member.isSameEmail(fSchedule.getCreatedBy())) {
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fScheduleRepository.update(fScheduleUpdate);
    }
}