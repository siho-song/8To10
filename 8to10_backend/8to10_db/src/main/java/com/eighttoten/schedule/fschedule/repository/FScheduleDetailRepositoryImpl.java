package com.eighttoten.schedule.fschedule.repository;

import com.eighttoten.schedule.domain.fschedule.FDetailUpdate;
import com.eighttoten.schedule.domain.fschedule.NewFScheduleDetail;
import com.eighttoten.schedule.fschedule.FScheduleDetailEntity;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.schedule.domain.fschedule.FScheduleDetail;
import com.eighttoten.schedule.domain.fschedule.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.fschedule.FScheduleEntity;
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
    public void save(NewFScheduleDetail newFScheduleDetail) {
        FScheduleEntity fScheduleEntity = fScheduleRepository.findById(newFScheduleDetail.getFScheduleId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_SCHEDULE));

        fScheduleDetailRepository.save(FScheduleDetailEntity.from(newFScheduleDetail,fScheduleEntity));
    }

    @Override
    public void update(FDetailUpdate fDetailUpdate) {
        FScheduleDetailEntity entity = fScheduleDetailRepository.findById(fDetailUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_F_DETAIL));
        entity.update(fDetailUpdate.getDetailDescription(), fDetailUpdate.getStartDateTime(),
                fDetailUpdate.getEndDateTime());
    }

    @Override
    public void deleteById(Long id) {
        fScheduleDetailRepository.deleteById(id);
    }

    @Override
    public Optional<FScheduleDetail> findById(Long id) {
        return fScheduleDetailRepository.findById(id).map(FScheduleDetailEntity::toFScheduleDetail);
    }

    @Override
    public List<FScheduleDetail> findAllBetweenStartAndEnd(String memberEmail, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<FScheduleDetailEntity> entities = fScheduleDetailRepository.findAllBetweenStartAndEnd(memberEmail,
                startDateTime, endDateTime);
        return entities.stream().map(FScheduleDetailEntity::toFScheduleDetail).toList();
    }

    @Override
    public List<FScheduleDetail> findByStartDateGEAndEmailAndParentId(
            LocalDateTime start,
            String email,
            Long parentId)
    {
        List<FScheduleDetailEntity> entities = fScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(
                start, email, parentId);
        return entities.stream().map(FScheduleDetailEntity::toFScheduleDetail).toList();
    }

    @Override
    public List<FScheduleDetail> findAllWithParentByMemberEmail(String memberEmail) {
        List<FScheduleDetailEntity> entities = fScheduleDetailRepository.findAllWithParentByMemberEmail(
                memberEmail);
        return entities.stream().map(FScheduleDetailEntity::toFScheduleDetail).toList();
    }

    public void deleteByIds(List<Long> ids) {
        fScheduleDetailRepository.deleteByIds(ids);
    }
}
