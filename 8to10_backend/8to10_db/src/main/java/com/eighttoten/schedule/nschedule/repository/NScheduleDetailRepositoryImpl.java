package com.eighttoten.schedule.nschedule.repository;

import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NewNDetail;
import com.eighttoten.schedule.nschedule.NScheduleDetailEntity;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.nschedule.NScheduleEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NScheduleDetailRepositoryImpl implements NScheduleDetailRepository {
    private final NScheduleDetailJpaRepository nScheduleDetailRepository;
    private final NScheduleJpaRepository nScheduleRepository;

    @Override
    public void deleteById(Long id) {
        nScheduleDetailRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        nScheduleDetailRepository.deleteByIds(ids);
    }

    @Override
    public void saveAll(Long nScheduleId, List<NewNDetail> newNDetails) {
        NScheduleEntity nScheduleEntity = nScheduleRepository.findById(nScheduleId)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_SCHEDULE));

        List<NScheduleDetailEntity> entities = newNDetails.stream()
                .map(nDetail -> NScheduleDetailEntity.from(nDetail, nScheduleEntity)).toList();
        nScheduleDetailRepository.saveAll(entities);
    }

    @Override
    public void update(NScheduleDetail nScheduleDetail) {
        NScheduleDetailEntity entity = nScheduleDetailRepository.findById(nScheduleDetail.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_N_DETAIL));
        entity.update(nScheduleDetail);
    }

    @Override
    public Optional<NScheduleDetail> findById(Long id) {
        return nScheduleDetailRepository.findById(id).map(NScheduleDetailEntity::toNScheduleDetail);
    }

    @Override
    public Optional<NDetailWithParent> findByIdWithParent(Long id) {
        return nScheduleDetailRepository.findByIdWithParent(id).map(NScheduleDetailEntity::toNDetailWithParent);
    }

    @Override
    public List<NScheduleDetail> findByStartDateGEAndEmailAndParentId(LocalDateTime start, String email,
                                                                      Long parentId) {
        List<NScheduleDetailEntity> entities = nScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(
                start, email, parentId);
        return entities.stream().map(NScheduleDetailEntity::toNScheduleDetail).toList();
    }

    @Override
    public List<NScheduleDetail> findAllByEmailAndDate(String email, LocalDate date) {
        List<NScheduleDetailEntity> entities = nScheduleDetailRepository.findAllByEmailAndDate(email, date);
        return entities.stream().map(NScheduleDetailEntity::toNScheduleDetail).toList();
    }

    @Override
    public List<NScheduleDetail> findAllByIds(List<Long> ids) {
        List<NScheduleDetailEntity> entities = nScheduleDetailRepository.findAllByIds(ids);
        return entities.stream().map(NScheduleDetailEntity::toNScheduleDetail).toList();
    }

    @Override
    public List<NScheduleDetail> findAllBetweenStartAndEnd(String memberEmail, LocalDateTime startDateTime,
                                                           LocalDateTime endDateTime) {
        List<NScheduleDetailEntity> entities = nScheduleDetailRepository.findAllBetweenStartAndEnd(memberEmail,
                startDateTime, endDateTime);
        return entities.stream().map(NScheduleDetailEntity::toNScheduleDetail).toList();
    }

    @Override
    public List<NDetailWithParent> findAllWithParentByMemberEmail(String memberEmail) {
        List<NScheduleDetailEntity> entities = nScheduleDetailRepository.findAllWithParentByMemberEmail(memberEmail);
        return entities.stream().map(NScheduleDetailEntity::toNDetailWithParent).toList();
    }
}
