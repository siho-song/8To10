package com.eighttoten.schedule.dto.request;

import com.eighttoten.schedule.domain.nschedule.NDetailUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NDetailUpdateRequest {
    @NotNull
    private Long id;
    private String detailDescription;

    public NDetailUpdate toNDetailUpdate(){
        return new NDetailUpdate(id, detailDescription);
    }
}