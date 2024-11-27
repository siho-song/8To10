package com.eighttoten.repository.achievement;

import com.eighttoten.domain.achievement.Achievement;
import com.eighttoten.domain.member.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByMemberAndAchievementDate(Member member, LocalDate achievementDate);

    @Query("select a from Achievement a where a.member = :member and a.achievementDate between :start and :end")
    List<Achievement> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);

    @Query("select a from Achievement a where a.achievementDate = :date")
    @EntityGraph(attributePaths = {"member"})
    List<Achievement> findAllByDateWithMember(@Param(value = "date") LocalDate date);
}