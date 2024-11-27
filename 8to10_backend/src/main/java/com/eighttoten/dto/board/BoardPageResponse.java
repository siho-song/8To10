package com.eighttoten.dto.board;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.eighttoten.domain.board.Board;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public static BoardPageResponse from(Board board){
        return BoardPageResponse.builder()
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
