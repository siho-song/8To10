package com.eighttoten.community.dto;

import com.eighttoten.community.domain.Reply;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplySearchResponse {
    private Long id;
    private Long parentId;
    private String contents;
    private String nickname; // 닉네임
    private String writer; // 이메일
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;

    public static ReplySearchResponse from(Reply reply){
        return ReplySearchResponse.builder()
                .id(reply.getId())
                .parentId(setParentId(reply))
                .contents(reply.getContents())
                .nickname(reply.getMember().getNickname())
                .writer(reply.getMember().getEmail())
                .totalLike(reply.getTotalLike())
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