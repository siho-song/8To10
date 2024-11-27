package com.eighttoten.domain.schedule.nschedule;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.ScheduleAble;
import com.eighttoten.dto.schedule.request.nschedule.NScheduleSave;
import com.eighttoten.dto.schedule.request.nschedule.NScheduleUpdate;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "N")
@ToString(callSuper = true)
public class NSchedule extends Schedule{
    @Column(nullable = false)
    private LocalTime bufferTime;

    private int totalAmount;

    @OneToMany(mappedBy = "nSchedule", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NScheduleDetail> nScheduleDetails = new ArrayList<>();

    public static NSchedule createNSchedule(Member member, NScheduleSave nScheduleSave){
        NSchedule nSchedule = new NSchedule();
        nSchedule.member = member;
        nSchedule.title = nScheduleSave.getTitle();
        nSchedule.commonDescription = nScheduleSave.getCommonDescription();
        nSchedule.startDate = LocalDateTime.of(nScheduleSave.getStartDate(),LocalTime.of(0,0));
        nSchedule.endDate = LocalDateTime.of(nScheduleSave.getEndDate(),LocalTime.of(0,0));
        nSchedule.bufferTime = nScheduleSave.getBufferTime();
        nSchedule.totalAmount = nScheduleSave.getTotalAmount();
        return nSchedule;
    }

    @Override
    public List<ScheduleAble> getScheduleAbles() {
        return nScheduleDetails.stream().map(nScheduleDetail -> (ScheduleAble) nScheduleDetail).toList();
    }

    public void update(NScheduleUpdate nScheduleUpdate){
        title = nScheduleUpdate.getTitle();
        commonDescription = nScheduleUpdate.getCommonDescription();
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