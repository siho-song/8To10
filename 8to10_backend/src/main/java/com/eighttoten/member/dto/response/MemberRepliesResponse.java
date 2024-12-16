package com.eighttoten.member.dto.response;

import com.eighttoten.community.domain.Reply;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberRepliesResponse {

    private Long boardId;
    private Long replyId;
    private String contents;
    private long totalLike;
    private LocalDateTime createdAt;

    public static MemberRepliesResponse from(Reply reply){
        MemberRepliesResponse memberRepliesResponse = new MemberRepliesResponse();
        memberRepliesResponse.replyId = reply.getId();
        memberRepliesResponse.boardId = reply.getBoard().getId();
        memberRepliesResponse.contents = reply.getContents();
        memberRepliesResponse.totalLike = reply.getTotalLike();
        memberRepliesResponse.createdAt = reply.getCreatedAt();

        return memberRepliesResponse;
    }
}