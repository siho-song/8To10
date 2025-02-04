package com.eighttoten.member.dto.response;

import com.eighttoten.community.domain.post.Post;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScrappedPostResponse {
    Long postId;
    String title;
    long totalLike;
    LocalDateTime createdAt;

    public static ScrappedPostResponse from(Post post){
        ScrappedPostResponse response = new ScrappedPostResponse();
        response.postId = post.getId();
        response.title = post.getTitle();
        response.totalLike = post.getTotalLike();
        response.createdAt = post.getCreatedAt();
        return response;
    }
}