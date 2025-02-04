package com.eighttoten.member.dto.response;

import com.eighttoten.community.domain.post.Post;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WrittenPostResponse {
    private Long postId;
    private String title;
    private long totalLike;
    private long totalScrap;
    private LocalDateTime createdAt;

    public static WrittenPostResponse from(Post post){
        WrittenPostResponse writtenPostResponse = new WrittenPostResponse();
        writtenPostResponse.postId = post.getId();
        writtenPostResponse.title = post.getTitle();
        writtenPostResponse.totalLike = post.getTotalLike();
        writtenPostResponse.totalScrap = post.getTotalScrap();
        writtenPostResponse.createdAt = post.getCreatedAt();

        return writtenPostResponse;
    }
}