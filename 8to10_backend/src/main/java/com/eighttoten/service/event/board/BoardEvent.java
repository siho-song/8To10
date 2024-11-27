package com.eighttoten.service.event.board;

import lombok.Getter;
import com.eighttoten.domain.board.Board;

@Getter
public abstract class BoardEvent {
    protected final Long boardId;

    public BoardEvent(Long id) {
        this.boardId = id;
    }

    public abstract void execute(Board board);
}
