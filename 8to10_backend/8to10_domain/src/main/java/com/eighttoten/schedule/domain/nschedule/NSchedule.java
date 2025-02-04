package com.eighttoten.schedule.domain.nschedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NSchedule {
    private Long id;
    private Long memberId;
    private String title;
    private String commonDescription;
    private String createdBy;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private int totalAmount;

    public NSchedule(
            Long id, Long memberId, String title,
            String commonDescription, String createdBy, LocalDateTime startDateTime, LocalDateTime endDateTime,
            LocalTime bufferTime, int totalAmount) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.commonDescription = commonDescription;
        this.createdBy = createdBy;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.bufferTime = bufferTime;
        this.totalAmount = totalAmount;
    }

    public void update(NScheduleUpdate nScheduleUpdate) {
        this.title = nScheduleUpdate.getTitle();
        this.commonDescription = nScheduleUpdate.getCommonDescription();
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
