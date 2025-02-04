package com.eighttoten.community.domain.reply;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Reply {
    private Long id;
    private Long parentId;
    private Long postId;
    private String contents;
    private String createdBy;
    private LocalDateTime createdAt;
    private long totalLike;

    public void addLike() {
        totalLike++;
    }
    public void subLike() {
        totalLike--;
    }

    public void update(UpdateReply updateReply) {
        this.contents = updateReply.getContents();
    }
}
