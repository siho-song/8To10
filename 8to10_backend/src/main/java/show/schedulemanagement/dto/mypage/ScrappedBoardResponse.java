package show.schedulemanagement.dto.mypage;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.domain.board.Board;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ScrappedBoardResponse {
    Long boardId;
    String title;
    long totalLike;
    LocalDateTime createdAt;

    public static ScrappedBoardResponse from(Board board){
        ScrappedBoardResponse response = new ScrappedBoardResponse();
        response.boardId = board.getId();
        response.title = board.getTitle();
        response.totalLike = board.getTotalLike();
        response.createdAt = board.getCreatedAt();
        return response;
    }
}
