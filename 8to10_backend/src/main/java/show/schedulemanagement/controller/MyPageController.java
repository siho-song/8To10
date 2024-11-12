package show.schedulemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.service.MyPageService;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@CurrentMember Member member) {
        return ResponseEntity.ok(myPageService.getProfile(member));
    }

    @GetMapping("/boards")
    public ResponseEntity<Result<MemberBoardsResponse>> getBoards(@CurrentMember Member member) {
        return ResponseEntity.ok(myPageService.getMemberBoards(member));
    }
}
