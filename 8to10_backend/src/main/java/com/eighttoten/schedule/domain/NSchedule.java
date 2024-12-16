package com.eighttoten.schedule.domain;

import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.member.domain.Member;
import com.eighttoten.schedule.dto.request.NScheduleSaveRequestRequest;
import com.eighttoten.schedule.dto.request.NScheduleUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "N")
@Table(name = "n_schedule")
public class NSchedule extends Schedule{
    @Column(nullable = false)
    private LocalTime bufferTime;

    @OneToMany(mappedBy = "nSchedule", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NScheduleDetail> nScheduleDetails = new ArrayList<>();

    private int totalAmount;

    public static NSchedule from(Member member, NScheduleSaveRequestRequest dto){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = dto.getTitle();
        nSchedule.commonDescription = dto.getCommonDescription();
        nSchedule.startDate = LocalDateTime.of(dto.getStartDate(),LocalTime.of(0,0));
        nSchedule.endDate = LocalDateTime.of(dto.getEndDate(),LocalTime.of(0,0));
        nSchedule.bufferTime = dto.getBufferTime();
        nSchedule.totalAmount = dto.getTotalAmount();
        return nSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return nScheduleDetails.stream().map(nScheduleDetail -> (ScheduleAble) nScheduleDetail).toList();
    }

    public void update(NScheduleUpdateRequest nScheduleUpdateRequest){
        title = nScheduleUpdateRequest.getTitle();
        commonDescription = nScheduleUpdateRequest.getCommonDescription();
    }

    public void updateTotalAmount(boolean isMinus, double dailyAmount) {
        if (isAmountValid(dailyAmount)) {
            int adjustedAmount = (int) Math.round(dailyAmount);
            if (isMinus) {
                totalAmount -= adjustedAmount;
            } else {
                totalAmount += adjustedAmount;
            }
        }
    }

    private boolean isAmountValid(double amount) {
        final double EPSILON = 1e-10;
        return totalAmount != 0 && Math.abs(amount) > EPSILON;
    }
}