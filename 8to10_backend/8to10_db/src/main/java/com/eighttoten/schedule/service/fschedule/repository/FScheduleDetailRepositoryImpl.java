package com.eighttoten.schedule.service.fschedule.repository;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.schedule.domain.fschedule.FDetailWithParent;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.NewFDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.service.fschedule.FScheduleDetailEntity;
import com.eighttoten.schedule.service.fschedule.FScheduleEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FScheduleDetailRepositoryImpl implements FScheduleDetailRepository {
    private final FScheduleDetailJpaRepository fScheduleDetailRepository;
    private final FScheduleJpaRepository fScheduleRepository;

    @Override
    public void save(NewFDetail newFDetail) {
        FScheduleEntity fScheduleEntity = fScheduleRepository.findById(newFDetail.getFScheduleId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_SCHEDULE));

        fScheduleDetailRepository.save(FScheduleDetailEntity.from(newFDetail,fScheduleEntity));
    }

    @Override
    public void update(FScheduleDetail fScheduleDetail) {
        FScheduleDetailEntity entity = fScheduleDetailRepository.findById(fScheduleDetail.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_DETAIL));

        entity.update(fScheduleDetail);
    }

    @Override
    public void deleteById(Long id) {
        fScheduleDetailRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        fScheduleDetailRepository.deleteAllByIds(ids);
    }

    @Override
    public Optional<FScheduleDetail> findById(Long id) {
        return fScheduleDetailRepository.findById(id).map(FScheduleDetailEntity::toFScheduleDetail);
    }

    @Override
    public List<FScheduleDetail> findAllByEmailBetweenStartAndEnd(String email, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<FScheduleDetailEntity> entities = fScheduleDetailRepository.findAllBetweenStartAndEnd(email,
                startDateTime, endDateTime);
        return entities.stream().map(FScheduleDetailEntity::toFScheduleDetail).toList();
    }

    @Override
    public List<FScheduleDetail> findAllByEmailAndParentIdGEStartDate(
            String email,
            Long parentId,
            LocalDateTime start)
    {
        List<FScheduleDetailEntity> entities = fScheduleDetailRepository.findAllByStartDateGEAndEmailAndParentId(
                start, email, parentId);
        return entities.stream().map(FScheduleDetailEntity::toFScheduleDetail).toList();
    }

    @Override
    public List<FDetailWithParent> findAllWithParentByMemberEmail(String email) {
        List<FScheduleDetailEntity> entities = fScheduleDetailRepository.findAllWithParentByMemberEmail(
                email);
        return entities.stream().map(FScheduleDetailEntity::toFDetailWithParent).toList();
    }
}
