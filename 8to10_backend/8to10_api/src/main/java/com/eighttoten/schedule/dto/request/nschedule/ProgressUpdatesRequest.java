package com.eighttoten.schedule.dto.request.nschedule;

import com.eighttoten.schedule.domain.ProgressUpdates;
import com.eighttoten.schedule.domain.ProgressUpdates.ProgressUpdate;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class ProgressUpdatesRequest {
    @NotNull
    private LocalDate date;
    private List<ProgressUpdateRequest> progressUpdateRequests;

    public ProgressUpdates toProgressUpdates(){
        return new ProgressUpdates(date,
                progressUpdateRequests.stream()
                        .map(ProgressUpdateRequest::toProgressUpdate)
                        .toList()
        );
    }

    @Getter
    public static class ProgressUpdateRequest {
        @NotNull
        private Long scheduleDetailId;
        private boolean completeStatus;
        private int achievedAmount;

        public ProgressUpdate toProgressUpdate(){
            return new ProgressUpdate(scheduleDetailId, completeStatus, achievedAmount);
        }
    }
}