package com.eighttoten;

import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.domain.Role;

public class TestDataUtils {
    public static Member createTestMember() {
        return new Member(null, "test", "test", "test", "test",
                "test", Gender.MALE, Role.NORMAL_USER, Mode.MILD,
                "test", 0, false, false);
    }

    public static Reply createTestReply(Long parentId, String createdBy){
        return new Reply(null, parentId, "테스트 댓글", createdBy, null, 0);
    }
    public static Post createTestPost(Member member) {
        return new Post(null, "테스트 데이터 내용", "테스트 데이터", member.getEmail(), null, 0, 0);
    }

    public static PostScrap createTestPostScrap(Member member, Post post) {
        return new PostScrap(null, post, member, member.getEmail());
    }
}