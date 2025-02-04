package com.eighttoten.schedule.fschedule.repository;

import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import com.eighttoten.schedule.domain.fschedule.NewFSchedule;
import com.eighttoten.schedule.fschedule.FScheduleEntity;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.schedule.domain.fschedule.FSchedule;
import com.eighttoten.schedule.domain.fschedule.FScheduleUpdate;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FScheduleRepositoryImpl implements FScheduleRepository {
    private final MemberJpaRepository memberRepository;
    private final FScheduleJpaRepository fScheduleRepository;

    @Override
    public void deleteById(Long id) {
        fScheduleRepository.deleteById(id);
    }

    @Override
    public void update(FScheduleUpdate fScheduleUpdate) {
        FScheduleEntity entity = fScheduleRepository.findById(fScheduleUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_SCHEDULE));
        entity.update(fScheduleUpdate.getTitle(), fScheduleUpdate.getCommonDescription());
    }

    @Override
    public long save(NewFSchedule newFSchedule) {
        MemberEntity memberEntity = memberRepository.findById(newFSchedule.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        FScheduleEntity savedEntity = fScheduleRepository.save(FScheduleEntity.from(newFSchedule, memberEntity));
        return savedEntity.getId();
    }

    @Override
    public Optional<FSchedule> findById(Long id) {
        return fScheduleRepository.findById(id).map(FScheduleEntity::toFSchedule);
    }
}