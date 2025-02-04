package com.eighttoten.community.dto.reply;

import com.eighttoten.community.domain.reply.ReplyDetailInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplySearchHttpResponse {
    private Long id;
    private Long parentId;
    private String contents;
    private String nickname; // 닉네임
    private String writer; // 이메일
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;

    public static ReplySearchHttpResponse from(ReplyDetailInfo response){
        return ReplySearchHttpResponse.builder()
                .id(response.getId())
                .parentId(response.getParentId())
                .contents(response.getContents())
                .nickname(response.getNickname())
                .writer(response.getWriter())
                .totalLike(response.getTotalLike())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .build();
    }
}