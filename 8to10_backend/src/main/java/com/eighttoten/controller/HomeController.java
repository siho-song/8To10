package com.eighttoten.controller;

import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.UserStatsResponse;
import com.eighttoten.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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