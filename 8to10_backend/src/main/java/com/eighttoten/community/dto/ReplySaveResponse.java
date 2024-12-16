package com.eighttoten.community.dto;

import com.eighttoten.community.domain.Reply;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplySaveResponse {
    private Long replyId;
    private Long parentId;
    private Long boardId;
    private String contents;
    private String nickname;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReplySaveResponse from(Reply reply) {
        return ReplySaveResponse.builder()
                .replyId(reply.getId())
                .parentId(setParentId(reply))
                .boardId(reply.getBoard().getId())
                .contents(reply.getContents())
                .nickname(reply.getMember().getNickname())
                .writer(reply.getMember().getEmail())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }

    private static Long setParentId(Reply reply) {
        if (reply.getParent() != null) {
            return reply.getParent().getId();
        }
        return null;
    }
}