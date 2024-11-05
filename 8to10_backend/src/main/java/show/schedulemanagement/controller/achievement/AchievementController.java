package show.schedulemanagement.controller.achievement;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.achievement.AchievementResponse;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.achievement.AchievementService;

@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor
@Slf4j
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping(value = "/{year}/{month}" , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<AchievementResponse>> getMonthAchievement(
            @CurrentMember Member member,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "month") int month)
    {
        List<Achievement> achievements = achievementService.findAllBetweenYearAndMonth(
                member,
                year,
                month
        );

        Result<AchievementResponse> result = Result.fromElements(
                achievements,
                AchievementResponse::from
        );

        log.debug("result : {} ", result);

        return ResponseEntity.ok(result);
    }

}
