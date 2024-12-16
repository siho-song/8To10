package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Board;
import lombok.Getter;

@Getter
public abstract class BoardEvent {
    protected final Long boardId;

    public BoardEvent(Long id) {
        this.boardId = id;
    }

    public abstract void execute(Board board);
}
