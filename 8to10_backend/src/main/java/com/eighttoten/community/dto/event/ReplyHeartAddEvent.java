package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Reply;

public class ReplyHeartAddEvent extends ReplyEvent {

    public ReplyHeartAddEvent(Long replyId) {
        super(replyId);
    }

    @Override
    public void execute(Reply reply) {
        reply.addLike();
    }
}
