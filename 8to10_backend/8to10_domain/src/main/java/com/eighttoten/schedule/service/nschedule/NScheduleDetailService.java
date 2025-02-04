package com.eighttoten.schedule.service.nschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_EXIST_N_DETAIL;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_DETAIL;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_SCHEDULE;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.event.ProgressUpdatedEvent;
import com.eighttoten.schedule.domain.ProgressUpdates;
import com.eighttoten.schedule.domain.ProgressUpdates.ProgressUpdate;
import com.eighttoten.schedule.domain.nschedule.NDetailUpdate;
import com.eighttoten.schedule.domain.nschedule.NDetailWithParent;
import com.eighttoten.schedule.domain.nschedule.NSchedule;
import com.eighttoten.schedule.domain.nschedule.NScheduleCreateInfo;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.NewNDetail;
import com.eighttoten.schedule.domain.nschedule.TimeSlot;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NScheduleDetailService {
    private final NScheduleDetailRepository nScheduleDetailRepository;
    private final NScheduleRepository nScheduleRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void saveDetails(
            Long nScheduleId,
            Map<DayOfWeek, TimeSlot> selectedTimeSlots,
            NScheduleCreateInfo nScheduleCreateInfo) {

        NSchedule nSchedule = nScheduleRepository.findById(nScheduleId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));

        LocalDateTime current = nSchedule.getStartDateTime();
        LocalDateTime end = nSchedule.getEndDateTime();
        LocalTime bufferTime = nScheduleCreateInfo.getBufferTime();
        LocalTime performInDay = nScheduleCreateInfo.getPerformInDay();

        List<NewNDetail> newNDetails = new ArrayList<>();
        while(!current.isAfter(end)){
            LocalDateTime startDate = current;
            DayOfWeek day = current.getDayOfWeek();
            TimeSlot timeSlot = selectedTimeSlots.get(day);
            if(timeSlot != null){
                LocalDateTime startDateTime = startDate.plusHours(
                                timeSlot.getStartTime().getHour() + bufferTime.getHour())
                        .plusMinutes(timeSlot.getStartTime().getMinute() + bufferTime.getMinute());

                LocalDateTime endDateTime = startDateTime.plusHours(performInDay.getHour())
                        .plusMinutes(performInDay.getMinute());

                newNDetails.add(NewNDetail.builder()
                        .nScheduleId(nSchedule.getId())
                        .startDateTime(startDateTime)
                        .endDateTime(endDateTime)
                        .bufferTime(bufferTime)
                        .detailDescription(nSchedule.getCommonDescription())
                        .build());
            }
            current = current.plusDays(1L);
        }
        setScheduleDailyAmount(newNDetails, nSchedule.getTotalAmount());
        nScheduleDetailRepository.saveAll(nScheduleId,newNDetails);
    }

    @Transactional
    public void update(Member member, NDetailUpdate nDetailUpdate){
        NScheduleDetail nScheduleDetail = nScheduleDetailRepository.findById(nDetailUpdate.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));

        if(!member.isSameEmail(nScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        nScheduleDetail.updateDetailDescription(nDetailUpdate.getDetailDescription());
        nScheduleDetailRepository.update(nScheduleDetail);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        NDetailWithParent nScheduleDetail = nScheduleDetailRepository.findByIdWithParent(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));;
        if(!member.isSameEmail(nScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }

        NSchedule parent = nScheduleDetail.getNSchedule();
        parent.updateTotalAmount(true, nScheduleDetail.getDailyAmount());
        nScheduleRepository.update(parent);
        nScheduleDetailRepository.deleteById(nScheduleDetail.getId());
    }

    @Transactional
    public void deleteByStartDateGEAndMemberAndParentId(
            LocalDateTime startDate,
            Member member,
            Long parentId)
    {
        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(
                startDate,
                member.getEmail(),
                parentId);
        NSchedule parent = nScheduleRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_SCHEDULE));

        parent.updateTotalAmount(true, getDailyAmountSum(nScheduleDetails));
        nScheduleDetailRepository.deleteByIds(nScheduleDetails.stream().map(NScheduleDetail::getId).toList());
        nScheduleRepository.update(parent);
    }

    @Transactional
    public void updateProgressList(Member member, ProgressUpdates progressUpdates) {
        List<ProgressUpdate> allProgress = progressUpdates.getProgressUpdates();
        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByIds(progressUpdates.getIds());
        allProgress.forEach(progressUpdate -> updateProgress(progressUpdate, nScheduleDetails));
        publisher.publishEvent(ProgressUpdatedEvent.createdEvent(member, progressUpdates.getDate()));
    }

    private void setScheduleDailyAmount(
            List<NewNDetail> newNDetails,
            int totalAmount) {

        int size = newNDetails.size();
        int div = totalAmount / size;
        int mod = totalAmount % size;

        for (int i = 0; i < size; i++) {
            if (i < mod){
                newNDetails.get(i).setDailyAmount(div + 1);
            } else {
                newNDetails.get(i).setDailyAmount(div);
            }
        }
    }

    private boolean isValidAchievementAmount(int newAchievementAmount, int dailyAmount) {
        return newAchievementAmount >= 0 && dailyAmount > 0 && newAchievementAmount <= dailyAmount;
    }

    private double getDailyAmountSum(List<NScheduleDetail> nScheduleDetails) {
        return nScheduleDetails.stream().mapToDouble(NScheduleDetail::getDailyAmount).sum();
    }

    private void updateProgress(ProgressUpdate progressUpdate, List<NScheduleDetail> nScheduleDetails) {
        NScheduleDetail nScheduleDetail = nScheduleDetails.stream()
                .filter(nd -> nd.getId().equals(progressUpdate.getScheduleDetailId()))
                .findFirst().orElseThrow(() -> new NotFoundEntityException(NOT_EXIST_N_DETAIL));

        int dailyAmount = nScheduleDetail.getDailyAmount();
        int newAchievementAmount = progressUpdate.getAchievedAmount();

        if (dailyAmount == 0) {
            nScheduleDetail.updateCompleteStatus(progressUpdate.isCompleteStatus());
        }

        if (isValidAchievementAmount(newAchievementAmount, dailyAmount)) {
            nScheduleDetail.updateAchievedAmount(newAchievementAmount);
            nScheduleDetail.updateCompleteStatus(nScheduleDetail.getAchievedAmount() == dailyAmount);
        }

        nScheduleDetailRepository.update(nScheduleDetail);
    }
}