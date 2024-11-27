package com.eighttoten.service.event.reply;

import lombok.Getter;
import com.eighttoten.domain.board.reply.Reply;

@Getter
public abstract class ReplyEvent {
    protected final Long replyId;

    public ReplyEvent(Long id) {
        this.replyId = id;
    }

    public abstract void execute(Reply reply);
}
