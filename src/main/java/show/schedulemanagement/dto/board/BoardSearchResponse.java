package show.schedulemanagement.dto.board;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.dto.board.reply.ReplyResponseDto;

@Data
@Builder
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
    private List<ReplyResponseDto> replies;

    public static BoardSearchResponse from(Board board){
        return BoardSearchResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContent())
                .nickname(board.getMember().getNickname())
                .writer(board.getMember().getEmail())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .totalLike(board.getTotalLike())
                .totalScrap(board.getTotalScrap())
                .replies(mapToReplyResponseDto(board))
                .build();
    }

    private static List<ReplyResponseDto> mapToReplyResponseDto(Board board){
        return board.getReplies().stream().map(ReplyResponseDto::from).toList();
    }
}
