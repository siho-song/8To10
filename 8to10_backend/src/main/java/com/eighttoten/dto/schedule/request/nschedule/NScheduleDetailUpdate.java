package com.eighttoten.dto.schedule.request.nschedule;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NScheduleDetailUpdate{
    @NotNull
    private Long id;
    private String detailDescription;
}