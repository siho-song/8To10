package com.eighttoten.community.domain.reply;

import com.eighttoten.community.domain.post.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyWithPost {
    private Long id;
    private Long parentId;
    private Post post;
    private String contents;
    private String createdBy;
    private LocalDateTime createdAt;
    private long totalLike;
}
