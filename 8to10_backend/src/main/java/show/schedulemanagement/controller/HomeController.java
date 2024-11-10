package show.schedulemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.UserStatsResponse;
import show.schedulemanagement.service.HomeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping(value = "/user-stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserStatsResponse> getUserStats(@CurrentMember Member member) {
        return ResponseEntity.ok(homeService.getDailyUserStats(member));
    }
}
