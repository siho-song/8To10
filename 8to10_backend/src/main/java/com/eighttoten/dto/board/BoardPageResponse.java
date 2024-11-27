package com.eighttoten.dto.board;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardPageResponse {
    private Long id;
    private String title;
    private String contents;
    private String writer;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;
    private long totalScrap;
}