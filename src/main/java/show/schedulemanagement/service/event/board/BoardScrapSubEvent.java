package show.schedulemanagement.service.event.board;

import show.schedulemanagement.domain.board.Board;

public class BoardScrapSubEvent extends BoardEvent{

    public BoardScrapSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.subScrap();
    }
}
