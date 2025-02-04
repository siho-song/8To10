package com.eighttoten.community.event.reply;

import com.eighttoten.community.domain.reply.Reply;

public class ReplyHeartSubEvent extends ReplyStatsUpdateEvent {
    public ReplyHeartSubEvent(Long postId) {
        super(postId);
    }

    @Override
    public void execute(Reply reply) {
        reply.subLike();
    }
}
