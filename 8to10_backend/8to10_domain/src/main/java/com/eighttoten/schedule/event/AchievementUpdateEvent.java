package com.eighttoten.schedule.event;

import com.eighttoten.member.domain.Member;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AchievementUpdateEvent {
    private Member member;
    private LocalDate date;
}
