package com.eighttoten.dto.board;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import com.eighttoten.domain.board.Board;

@Data
@Builder
public class BoardSaveResponse {
    private Long id;
    private String title;
    private String contents;
    private String writer;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;
    private long totalScrap;

    public static BoardSaveResponse from(Board board){
        return BoardSaveResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .writer(board.getMember().getEmail())
                .nickname(board.getMember().getNickname())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .totalLike(board.getTotalLike())
                .totalScrap(board.getTotalScrap())
                .build();
    }
}
