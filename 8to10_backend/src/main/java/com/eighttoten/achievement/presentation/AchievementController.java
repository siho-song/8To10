package com.eighttoten.achievement.presentation;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.achievement.domain.Achievement;
import com.eighttoten.achievement.dto.AchievementResponse;
import com.eighttoten.achievement.service.AchievementService;
import com.eighttoten.global.CurrentMember;
import com.eighttoten.global.dto.Result;
import com.eighttoten.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor
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

        return ResponseEntity.ok(result);
    }
}