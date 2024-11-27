package com.eighttoten.service.event;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import com.eighttoten.domain.member.Member;

@Getter
@Builder
public class ProgressUpdatedEvent {
    private Member member;
    private LocalDate date;

    public static ProgressUpdatedEvent createdEvent(
            Member member,
            LocalDate date)
    {
        return ProgressUpdatedEvent.builder()
                .member(member)
                .date(date)
                .build();
    }
}
