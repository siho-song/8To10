package com.eighttoten.dto.schedule.request.fschedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FScheduleUpdate {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 1,max = 80)
    private String title;
    private String commonDescription;
}
