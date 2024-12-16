package com.eighttoten.notification.event;

import com.eighttoten.member.domain.Member;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

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
