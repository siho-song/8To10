package com.eighttoten.member.dto.response;

import com.eighttoten.community.domain.reply.Reply;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WrittenReplyResponse {
    private Long postId;
    private Long replyId;
    private String contents;
    private long totalLike;
    private LocalDateTime createdAt;

    public static WrittenReplyResponse from(Reply reply){
        WrittenReplyResponse writtenReplyResponse = new WrittenReplyResponse();
        writtenReplyResponse.replyId = reply.getId();
        writtenReplyResponse.postId = reply.getPostId();
        writtenReplyResponse.contents = reply.getContents();
        writtenReplyResponse.totalLike = reply.getTotalLike();
        writtenReplyResponse.createdAt = reply.getCreatedAt();

        return writtenReplyResponse;
    }
}