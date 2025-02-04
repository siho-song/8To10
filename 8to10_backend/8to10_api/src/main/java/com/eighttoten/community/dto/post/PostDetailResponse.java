package com.eighttoten.community.dto.post;

import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.dto.reply.ReplySearchHttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostDetailResponse {
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
    private List<Long> likedReplyIds;
    private List<ReplySearchHttpResponse> replies;

    public static PostDetailResponse from(PostDetailInfo postDetailInfo) {
        PostDetailResponse httpResponse = new PostDetailResponse();
        httpResponse.id = postDetailInfo.getId();
        httpResponse.title = postDetailInfo.getTitle();
        httpResponse.contents = postDetailInfo.getContents();
        httpResponse.nickname = postDetailInfo.getNickname();
        httpResponse.writer = postDetailInfo.getWriter();
        httpResponse.createdAt = postDetailInfo.getCreatedAt();
        httpResponse.updatedAt = postDetailInfo.getUpdatedAt();
        httpResponse.totalLike = postDetailInfo.getTotalLike();
        httpResponse.totalScrap = postDetailInfo.getTotalScrap();
        httpResponse.hasLike = postDetailInfo.isHasLike();
        httpResponse.hasScrap = postDetailInfo.isHasScrap();
        httpResponse.likedReplyIds = postDetailInfo.getLikedReplyIds();
        httpResponse.replies = postDetailInfo.getReplies().stream().map(ReplySearchHttpResponse::from).toList();
        return httpResponse;
    }
}