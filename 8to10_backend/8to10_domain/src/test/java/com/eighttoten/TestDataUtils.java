package com.eighttoten;

import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.domain.Role;

public class TestDataUtils {
    public static Member createTestMember(Long id, String email) {
        return new Member(id, "test", "test", email, "test",
                "test", Gender.MALE, Role.NORMAL_USER, Mode.MILD,
                "test", 0, false, false);
    }

    public static Reply createTestReply(Long parentId, Long postId, String createdBy) {
        return new Reply(null, parentId, postId, "테스트 댓글", createdBy, null, 0);
    }

    public static Post createTestPost(Long id, Member member) {
        return new Post(id, "테스트 데이터 내용", "테스트 데이터", member.getEmail(), null, 0, 0);
    }
}