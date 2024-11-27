package com.eighttoten.controller;

import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.Result;
import com.eighttoten.dto.mypage.MemberBoardsResponse;
import com.eighttoten.dto.mypage.MemberRepliesResponse;
import com.eighttoten.dto.mypage.NicknameUpdateRequest;
import com.eighttoten.dto.mypage.PasswordUpdateRequest;
import com.eighttoten.dto.mypage.ProfileResponse;
import com.eighttoten.dto.mypage.ScrappedBoardResponse;
import com.eighttoten.service.MyPageService;

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

    @PutMapping(value = "/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProfilePhoto(
            @CurrentMember Member member,
            @RequestParam(name = "file") MultipartFile file) throws IOException {
        myPageService.uploadProfilePhoto(member, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/profile/photo")
    public ResponseEntity<Void> deleteProfilePhoto(@CurrentMember Member member) {
        myPageService.deleteProfilePhoto(member);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/account/nickname")
    public ResponseEntity<Void> updateNickname(
            @RequestBody @Valid NicknameUpdateRequest dto,
            @CurrentMember Member member)
    {
        myPageService.updateNickname(dto.getNickname(), member.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/account/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody @Valid PasswordUpdateRequest dto,
            @CurrentMember Member member)
    {
        myPageService.updatePassword(dto.getPassword(), member.getId());
        return ResponseEntity.noContent().build();
    }
}
