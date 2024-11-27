package com.eighttoten.dto.schedule.response.nschedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import com.eighttoten.domain.schedule.nschedule.NSchedule;

@Data
@Builder
public class NScheduleUpdateResponse {
    private Long id;
    private String title;
    private String commonDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime bufferTime;
    private Integer totalAmount;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public static NScheduleUpdateResponse from(NSchedule nSchedule){
        return NScheduleUpdateResponse.builder()
                .id(nSchedule.getId())
                .title(nSchedule.getTitle())
                .commonDescription(nSchedule.getCommonDescription())
                .startDate(nSchedule.getStartDate())
                .endDate(nSchedule.getEndDate())
                .bufferTime(nSchedule.getBufferTime())
                .totalAmount(nSchedule.getTotalAmount())
                .createdBy(nSchedule.getCreatedBy())
                .createdAt(nSchedule.getCreatedAt())
                .updatedBy(nSchedule.getUpdatedBy())
                .updatedAt(nSchedule.getUpdatedAt())
                .build();
    }
}
