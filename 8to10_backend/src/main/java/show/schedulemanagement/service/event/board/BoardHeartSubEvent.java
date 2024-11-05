package show.schedulemanagement.service.event.board;

import show.schedulemanagement.domain.board.Board;

public class BoardHeartSubEvent extends BoardEvent{

    public BoardHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.subLike();
    }
}
