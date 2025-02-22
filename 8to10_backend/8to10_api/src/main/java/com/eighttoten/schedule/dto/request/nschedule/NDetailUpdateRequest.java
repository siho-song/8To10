package com.eighttoten.schedule.dto.request.nschedule;

import com.eighttoten.schedule.domain.nschedule.NDetailUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NDetailUpdateRequest {
    @NotNull
    private Long id;
    private String detailDescription;

    public NDetailUpdate toNDetailUpdate(){
        return new NDetailUpdate(id, detailDescription);
    }
}