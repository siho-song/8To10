package com.eighttoten.schedule.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ProgressUpdates {
    private LocalDate date;
    private List<ProgressUpdate> progressUpdates;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProgressUpdate {
        private Long scheduleDetailId;
        private boolean completeStatus;
        private int achievedAmount;
    }

    public List<Long> getIds(){
        return progressUpdates.stream().map(ProgressUpdate::getScheduleDetailId).toList();
    }
}
