package show.schedulemanagement.dto.board;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import show.schedulemanagement.domain.board.Board;

@Data
@Builder
public class BoardResponseDto {
    private String title;
    private String contents;
    private String writer;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalLike;
    private long totalScrap;

    public static BoardResponseDto from(Board board){
        return BoardResponseDto.builder()
                .title(board.getTitle())
                .contents(board.getContent())
                .writer(board.getMember().getEmail())
                .nickname(board.getMember().getNickname())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .totalLike(board.getTotalLike())
                .totalScrap(board.getTotalScrap())
                .build();
    }
}
