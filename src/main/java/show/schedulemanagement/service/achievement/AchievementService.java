package show.schedulemanagement.service.achievement;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.nschedule.NScheduleDetail;
import show.schedulemanagement.repository.achievement.AchievementRepository;
import show.schedulemanagement.repository.schedule.nschedule.NScheduleDetailRepository;
import show.schedulemanagement.service.event.ProgressUpdatedEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final NScheduleDetailRepository nScheduleDetailRepository;

    //해당일의 성취도 조회
    //public void get() 없으면 응답에 reponse = null;

    @EventListener
    public void handleProgressUpdate(ProgressUpdatedEvent event) {
        LocalDate date = event.getDate();
        Member member = event.getMember();

        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findAllByDateAndEmail(
                date,
                member.getEmail());

        Achievement achievement = achievementRepository.findByAchievementDate(date)
                .orElse(Achievement.createAchievement(member, date, getAchievementRate(nScheduleDetails)));

        achievementRepository.save(achievement);
    }

    private double getAchievementRate(List<NScheduleDetail> nScheduleDetails){
        double achievementSum = nScheduleDetails.stream()
                .mapToDouble(NScheduleDetail::getAchievementRate)
                .sum();

        int size = nScheduleDetails.size();

        return achievementSum / size;
    }
}
