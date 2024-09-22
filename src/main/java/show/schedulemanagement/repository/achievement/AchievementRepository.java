package show.schedulemanagement.repository.achievement;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.achievement.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    //일 기준으로 유저의 성취도 조회
    Optional<Achievement> findByAchievementDate(LocalDate achievementDate);

    //달 기준으로 유저의 성취도 조회

}
