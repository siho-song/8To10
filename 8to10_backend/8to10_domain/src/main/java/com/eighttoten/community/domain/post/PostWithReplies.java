package com.eighttoten.community.domain.post;

import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public class PostWithReplies {
    private Long id;
    private Member member;
    private String title;
    private String contents;
    private String createdBy;
    private LocalDateTime createdAt;
    private long totalLike;
    private long totalScrap;
    private List<Reply> replies;
}
