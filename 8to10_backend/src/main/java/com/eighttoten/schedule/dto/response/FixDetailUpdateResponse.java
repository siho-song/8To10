package com.eighttoten.schedule.dto.response;

import com.eighttoten.schedule.domain.FScheduleDetail;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FixDetailUpdateResponse {
    private Long id;
    private String detailDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static FixDetailUpdateResponse from(FScheduleDetail fScheduleDetail){
        return FixDetailUpdateResponse.builder()
                .id(fScheduleDetail.getId())
                .detailDescription(fScheduleDetail.getDetailDescription())
                .startDate(fScheduleDetail.getStartDate())
                .endDate(fScheduleDetail.getEndDate())
                .build();
    }
}