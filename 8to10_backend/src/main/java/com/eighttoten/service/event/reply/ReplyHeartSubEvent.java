package com.eighttoten.service.event.reply;

import com.eighttoten.domain.board.reply.Reply;

public class ReplyHeartSubEvent extends ReplyEvent {

    public ReplyHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Reply reply) {
        reply.subLike();
    }
}
