package com.eighttoten.community.dto.post;

import com.eighttoten.community.domain.post.PostPreview;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostPreviewResponse {
    private Long id;
    private String title;
    private String contents;
    private String writer;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;
    private long totalScrap;

    public static PostPreviewResponse from(PostPreview postPreview) {
        PostPreviewResponse response = new PostPreviewResponse();
        response.id = postPreview.getId();
        response.title = postPreview.getTitle();
        response.contents = postPreview.getContents();
        response.writer = postPreview.getWriter();
        response.nickname = postPreview.getNickname();
        response.createdAt = postPreview.getCreatedAt();
        response.updatedAt = postPreview.getUpdatedAt();
        response.totalLike = postPreview.getTotalLike();
        response.totalScrap = postPreview.getTotalScrap();
        return response;
    }
}