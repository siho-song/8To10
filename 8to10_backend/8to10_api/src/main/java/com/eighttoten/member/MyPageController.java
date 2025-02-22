package com.eighttoten.member;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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

    @GetMapping("/scrapped-posts")
    public ResponseEntity<Result<ScrappedPostResponse>> getScrapedPosts(@CurrentMember Member member) {
        return ResponseEntity.ok(Result.fromElements(myPageService.getScrappedPost(member.getId()), ScrappedPostResponse::from));
    }

    @PutMapping(value = "/profile/photo", consumes = MULTIPART_FORM_DATA_VALUE)
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

    @PutMapping(value = "/account/nickname" , consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateNickname(
            @CurrentMember Member member,
            @RequestBody @Valid NicknameUpdateRequest request)
    {
        myPageService.updateNickname(member, request.getNickname());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/account/password", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePassword(
            @CurrentMember Member member,
            @RequestBody @Valid PasswordUpdateRequest request)
    {
        myPageService.updatePassword(member, request.getPassword());
        return ResponseEntity.noContent().build();
    }
}