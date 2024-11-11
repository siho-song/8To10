package show.schedulemanagement.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.UserStatsResponse;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.service.achievement.AchievementService;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeService {

    private final AchievementService achievementService;

    @Transactional(readOnly = true)
    public UserStatsResponse getDailyUserStats(Member member){
        Achievement achievement = achievementService.findByMemberAndDateIfExists(member, LocalDate.now());
        if(achievement != null) {
            return UserStatsResponse.of(member.getNickname(), member.getRole().getValue(),
                    achievement.getAchievementRate());
        }
        return UserStatsResponse.of(member.getNickname(), member.getRole().getValue(), 0);
    }
}
