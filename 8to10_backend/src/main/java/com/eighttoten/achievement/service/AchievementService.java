package com.eighttoten.achievement.service;

import com.eighttoten.achievement.domain.Achievement;
import com.eighttoten.achievement.domain.AchievementRepository;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.event.ProgressUpdatedEvent;
import com.eighttoten.schedule.domain.NScheduleDetail;
import com.eighttoten.schedule.domain.repository.NScheduleDetailRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final NScheduleDetailRepository nScheduleDetailRepository;

    public Achievement findByMemberAndDateIfExists(Member member, LocalDate date){
        return achievementRepository.findByMemberAndAchievementDate(member, date)
                .orElse(null);
    }

    public List<Achievement> findAllBetweenYearAndMonth(Member member, int year, int month){
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());

        return achievementRepository.findAllBetweenStartAndEnd(member, start, end);
    }

    public List<Achievement> findAllByDateWithMember(LocalDate date) {
        return achievementRepository.findAllByDateWithMember(date);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProgressUpdate(ProgressUpdatedEvent event) {
        LocalDate date = event.getDate();
        Member member = event.getMember();

        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByDateAndEmail(
                date,
                member.getEmail());

        Achievement achievement = achievementRepository.findByMemberAndAchievementDate(member, date)
                .orElse(Achievement.createAchievement(member, date));

        achievement.setAchievementRate(nScheduleDetails);
        achievementRepository.save(achievement);
    }
}