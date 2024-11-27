package com.eighttoten.dto.board;

import com.eighttoten.dto.board.reply.ReplySearchResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchResponse {
    private Long id;
    private String title;
    private String contents;
    private String nickname;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;
    private long totalScrap;
    private boolean hasLike;
    private boolean hasScrap;
    private List<Long> likedReplyIds;
    private List<ReplySearchResponse> replies;
}