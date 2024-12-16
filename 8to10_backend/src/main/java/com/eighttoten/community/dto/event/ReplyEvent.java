package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Reply;
import lombok.Getter;

@Getter
public abstract class ReplyEvent {
    protected final Long replyId;

    public ReplyEvent(Long id) {
        this.replyId = id;
    }

    public abstract void execute(Reply reply);
}
