package com.eighttoten.community.event.reply;

import com.eighttoten.community.domain.reply.Reply;

public class ReplyHeartAddEvent extends ReplyStatsUpdateEvent {
    public ReplyHeartAddEvent(Long replyId) {
        super(replyId);
    }

    @Override
    public void execute(Reply reply) {
        reply.addLike();
    }
}
