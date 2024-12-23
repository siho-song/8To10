package com.eighttoten.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NormalDetailUpdateRequest {
    @NotNull
    private Long id;
    private String detailDescription;
}