package com.eighttoten.member;

import com.eighttoten.member.domain.Member;
import com.eighttoten.member.dto.request.NicknameUpdateRequest;
import com.eighttoten.member.dto.request.PasswordUpdateRequest;
import com.eighttoten.member.dto.response.ProfileResponse;
import com.eighttoten.member.dto.response.ScrappedPostResponse;
import com.eighttoten.member.dto.response.WrittenPostResponse;
import com.eighttoten.member.dto.response.WrittenReplyResponse;
import com.eighttoten.member.service.MyPageService;
import com.eighttoten.support.CurrentMember;
import com.eighttoten.support.Result;
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

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@CurrentMember Member member) {
        return ResponseEntity.ok(ProfileResponse.from(member));
    }

    @GetMapping("/posts")
    public ResponseEntity<Result<WrittenPostResponse>> getWrittenPosts(@CurrentMember Member member) {
        return ResponseEntity.ok(Result.fromElements(myPageService.getWrittenPosts(member.getId()), WrittenPostResponse::from));
    }

    @GetMapping("/replies")
    public ResponseEntity<Result<WrittenReplyResponse>> getWrittenReplies(@CurrentMember Member member) {
        return ResponseEntity.ok(Result.fromElements(myPageService.getWrittenReplies(member.getId()), WrittenReplyResponse::from));
    }

    @GetMapping("/scrapped-boards")
    public ResponseEntity<Result<ScrappedPostResponse>> getScrapedPosts(@CurrentMember Member member) {
        return ResponseEntity.ok(Result.fromElements(myPageService.getScrappedPost(member.getId()), ScrappedPostResponse::from));
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

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteMember(@CurrentMember Member member){
        myPageService.deleteMember(member);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/account/nickname")
    public ResponseEntity<Void> updateNickname(
            @CurrentMember Member member,
            @RequestBody @Valid NicknameUpdateRequest request)
    {
        myPageService.updateNickname(member, request.getNickname());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/account/password")
    public ResponseEntity<Void> updatePassword(
            @CurrentMember Member member,
            @RequestBody @Valid PasswordUpdateRequest request)
    {
        myPageService.updatePassword(member, request.getPassword());
        return ResponseEntity.noContent().build();
    }
}