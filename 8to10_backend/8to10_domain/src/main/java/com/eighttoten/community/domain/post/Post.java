package com.eighttoten.community.domain.post;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String contents;
    private String createdBy;
    private LocalDateTime createdAt;
    private long totalLike;
    private long totalScrap;

    public boolean checkIsEqualId(Long postId) {
        return id.equals(postId);
    }

    public void addLike(){
        totalLike++;
    }
    public void subLike() { totalLike--; }
    public void addScrap() { totalScrap++; }
    public void subScrap() { totalScrap--; }

    public void update(UpdatePost updatePost) {
        title = updatePost.getTitle();
        contents = updatePost.getContents();
    }
}
