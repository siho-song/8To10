package com.eighttoten.schedule.dto.request;

import com.eighttoten.schedule.domain.nschedule.NScheduleUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NScheduleUpdateRequest {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 1,max = 80)
    private String title;
    private String commonDescription;

    public NScheduleUpdate toNScheduleUpdate(){
        return new NScheduleUpdate(id, title, commonDescription);
    }
}