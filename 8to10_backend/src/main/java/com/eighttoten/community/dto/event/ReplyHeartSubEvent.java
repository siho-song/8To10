package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Reply;

public class ReplyHeartSubEvent extends ReplyEvent {

    public ReplyHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Reply reply) {
        reply.subLike();
    }
}
