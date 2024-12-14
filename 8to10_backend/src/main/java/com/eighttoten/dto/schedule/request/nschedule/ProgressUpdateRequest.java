package com.eighttoten.dto.schedule.request.nschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressUpdateRequest{
    @NotNull
    private LocalDate date;
    private List<ProgressUpdate> progressUpdates;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProgressUpdate {
        @NotNull
        private Long scheduleDetailId;
        private boolean completeStatus;
        private int achievedAmount;
    }

    public List<Long> fetchIds(){
        return progressUpdates.stream().map(ProgressUpdate::getScheduleDetailId).toList();
    }
}