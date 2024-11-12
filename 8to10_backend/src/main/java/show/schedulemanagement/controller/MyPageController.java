package show.schedulemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.MemberRepliesResponse;
import show.schedulemanagement.dto.mypage.NicknameUpdateRequest;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.dto.mypage.ScrappedBoardResponse;
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

    @GetMapping("/replies")
    public ResponseEntity<Result<MemberRepliesResponse>> getReplies(@CurrentMember Member member) {
        return ResponseEntity.ok(myPageService.getMemberReplies(member));
    }

    @GetMapping("/scrapped-boards")
    public ResponseEntity<Result<ScrappedBoardResponse>> getScrapBoards(@CurrentMember Member member) {
        return ResponseEntity.ok(myPageService.getScrappedBoard(member));
    }

    @PutMapping("/account/nickname")
    public ResponseEntity<Void> updateNickname(
            @RequestBody @Valid NicknameUpdateRequest dto,
            @CurrentMember Member member)
    {
        myPageService.updateNickname(dto.getNickname(), member.getId());
        return ResponseEntity.noContent().build();
    }
}
