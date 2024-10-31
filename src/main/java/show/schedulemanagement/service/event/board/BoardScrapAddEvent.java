package show.schedulemanagement.service.event.board;

import show.schedulemanagement.domain.board.Board;

public class BoardScrapAddEvent extends BoardEvent{

    public BoardScrapAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.addScrap();
    }
}
