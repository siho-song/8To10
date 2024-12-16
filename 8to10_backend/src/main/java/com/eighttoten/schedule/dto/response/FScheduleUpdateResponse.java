package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.FSchedule;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FScheduleUpdateResponse {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public static FScheduleUpdateResponse from(FSchedule fSchedule){
        return FScheduleUpdateResponse.builder()
                .id(fSchedule.getId())
                .title(fSchedule.getTitle())
                .commonDescription(fSchedule.getCommonDescription())
                .startDate(fSchedule.getStartDate())
                .endDate(fSchedule.getEndDate())
                .createdBy(fSchedule.getCreatedBy())
                .createdAt(fSchedule.getCreatedAt())
                .updatedBy(fSchedule.getUpdatedBy())
                .updatedAt(fSchedule.getUpdatedAt())
                .build();
    }
}