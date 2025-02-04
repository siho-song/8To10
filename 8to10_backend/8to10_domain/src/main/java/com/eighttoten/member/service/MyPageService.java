package com.eighttoten.member.service;

import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.support.PasswordEncoder;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final PostScrapRepository postScrapRepository;
    private final MemberRepository memberRepository;
    private final MultipartFileStorageService multiPartFileStorageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Post> getWrittenPosts(Long memberId) {
        return postRepository.findAllByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Reply> getWrittenReplies(Long memberId) {
        return replyRepository.findAllByMemberId(memberId);
    }

    public List<Post> getScrappedPost(Long memberId) {
        List<PostScrap> postScraps = postScrapRepository.findAllByMemberIdWithPost(memberId);
        return postScraps.stream().map(PostScrap::getPost).toList();
    }

    @Transactional
    public void uploadProfilePhoto(Member member, MultipartFile file) throws IOException {
        String savedFilePath = multiPartFileStorageService.saveImageFile(file);
        member.updateProfilePhoto(savedFilePath);
        memberRepository.update(member);
    }

    @Transactional
    public void updateNickname(Member member, String nickname) {
        member.updateNickname(nickname);
        memberRepository.update(member);
    }

    @Transactional
    public void updatePassword(Member member, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        member.updatePassword(encodedPassword);
        memberRepository.update(member);
    }

    @Transactional
    public void deleteProfilePhoto(Member member) {
        String imageFileUrl = member.getProfileImagePath();
        if(imageFileUrl != null){
            multiPartFileStorageService.deleteFile(imageFileUrl);
            member.updateProfilePhoto(null);
            memberRepository.update(member);
        }
    }
}