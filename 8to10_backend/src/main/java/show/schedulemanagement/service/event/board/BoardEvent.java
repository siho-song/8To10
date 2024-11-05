package show.schedulemanagement.service.event.board;

import lombok.Getter;
import show.schedulemanagement.domain.board.Board;

@Getter
public abstract class BoardEvent {
    protected final Long boardId;

    public BoardEvent(Long id) {
        this.boardId = id;
    }

    public abstract void execute(Board board);
}
