package show.schedulemanagement.dto.board;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.reply.ReplyHeart;
import show.schedulemanagement.dto.board.reply.ReplySearchResponse;

@Data
@NoArgsConstructor
public class BoardSearchResponse {
    private Long id;
    private String title;
    private String content;
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
