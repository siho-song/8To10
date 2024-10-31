package show.schedulemanagement.service.event.board;

import show.schedulemanagement.domain.board.Board;

public class BoardHeartAddEvent extends BoardEvent {

    public BoardHeartAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.addLike();
    }
}
