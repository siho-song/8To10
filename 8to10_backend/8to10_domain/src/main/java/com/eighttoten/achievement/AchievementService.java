package com.eighttoten.achievement;

import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.event.ProgressUpdatedEvent;
import com.eighttoten.schedule.domain.nschedule.NScheduleDetail;
import com.eighttoten.schedule.domain.nschedule.repository.NScheduleDetailRepository;
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

    public List<Achievement> getMonthlyAchievement(Long memberId, int year, int month){
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());
        return achievementRepository.findAllByMemberIdAndBetweenStartAndEnd(memberId, start, end);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProgressUpdate(ProgressUpdatedEvent event) {
        LocalDate date = event.getDate();
        Member member = event.getMember();

        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository
                .findAllByEmailAndDate(member.getEmail(), date);

        double achievementRate = calculateAchievementRate(nScheduleDetails);

        Achievement achievement = achievementRepository.findByMemberIdAndDate(member.getId(), date)
                .orElse(null);

        if(achievement == null) {
            NewAchievement newAchievement = NewAchievement.from(member.getId(), date, achievementRate);
            achievementRepository.save(newAchievement);
            return;
        }

        achievement.updateAchievementRate(achievementRate);
        achievementRepository.update(achievement);
    }

    private double calculateAchievementRate(List<NScheduleDetail> nScheduleDetails){
        double achievementSum = nScheduleDetails.stream()
                .mapToDouble(NScheduleDetail::getAchievementRate)
                .sum();

        int size = nScheduleDetails.size();
        return achievementSum / size;
    }
}