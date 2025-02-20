package com.eighttoten.schedule.domain.nschedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NSchedule {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime bufferTime;
    private String createdBy;
    private int totalAmount;

    public NSchedule(
            Long id, String title,
            String commonDescription, LocalDateTime startDateTime, LocalDateTime endDateTime,
            LocalTime bufferTime, String createdBy, int totalAmount) {
        this.id = id;
        this.title = title;
        this.commonDescription = commonDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.bufferTime = bufferTime;
        this.createdBy = createdBy;
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
