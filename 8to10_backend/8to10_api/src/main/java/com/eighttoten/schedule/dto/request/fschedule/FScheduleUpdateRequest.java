package com.eighttoten.schedule.dto.request.fschedule;

import com.eighttoten.schedule.domain.fschedule.FScheduleUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FScheduleUpdateRequest {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 1,max = 80)
    private String title;
    private String commonDescription;

    public FScheduleUpdate toFScheduleUpdate(){
        return new FScheduleUpdate(id, title, commonDescription);
    }
}
