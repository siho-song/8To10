package com.eighttoten.community.domain.post;

import com.eighttoten.community.domain.reply.ReplyDetailInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostDetailInfo {
    private Long id;
    private String title;
    private String contents;
    private String nickname;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;
    private long totalScrap;
    private boolean hasLike;
    private boolean hasScrap;
    @Setter
    private List<Long> likedReplyIds;
    @Setter
    private List<ReplyDetailInfo> replies;
}
