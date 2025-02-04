package com.eighttoten.achievement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AchievementRepository {
    void save(NewAchievement newAchievement);
    void update(Achievement achievement);
    Optional<Achievement> findByMemberIdAndDate(Long memberId, LocalDate achievementDate);
    List<Achievement> findAllByMemberIdAndBetween(Long memberId, LocalDate start, LocalDate end);
    List<AchievementWithMember> findAllByDateWithMember(LocalDate date);
}
