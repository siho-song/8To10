package com.eighttoten.dto.schedule.request.nschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProgressUpdateRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    private Long scheduleDetailId;
    private boolean isComplete;
    private int achievedAmount;
}