package com.eighttoten.schedule.service;

import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_F_DETAIL;
import static com.eighttoten.global.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.global.exception.MismatchException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.domain.FScheduleDetail;
import com.eighttoten.schedule.domain.repository.FScheduleDetailRepository;
import com.eighttoten.schedule.dto.request.FixDetailUpdateRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FScheduleDetailService {
    private final FScheduleDetailRepository fScheduleDetailRepository;

    public FScheduleDetail findById(Long id) {
        return fScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_F_DETAIL));
    }

    public List<FScheduleDetail> findByStartDateGEAndEmailAndParentId(
            LocalDateTime start,
            String email,
            Long parentId)
    {
        return fScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(start, email , parentId);
    }

    @Transactional
    public void deleteByFScheduleDetails(List<FScheduleDetail> fScheduleDetails){
        fScheduleDetailRepository.deleteByFScheduleDetails(fScheduleDetails);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fScheduleDetailRepository.delete(fScheduleDetail);
    }

    @Transactional
    public void update(Member member, FixDetailUpdateRequest fixDetailUpdateRequest){
        Long id = fixDetailUpdateRequest.getId();
        FScheduleDetail fScheduleDetail = findById(id);
        if(!member.getEmail().equals(fScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        fScheduleDetail.update(fixDetailUpdateRequest);
    }

    @Transactional
    public void deleteByStartDateGEAndMemberAndParentId(
            LocalDateTime start,
            Member member,
            Long parentId)
    {
        List<FScheduleDetail> fScheduleDetails = findByStartDateGEAndEmailAndParentId(start, member.getEmail(), parentId);
        deleteByFScheduleDetails(fScheduleDetails);
    }
}