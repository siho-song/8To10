package show.schedulemanagement.dto.board;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.board.Board;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {
    private Long id;
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
                .id(board.getId())
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
