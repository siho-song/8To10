package com.eighttoten.community.event.reply;

import com.eighttoten.community.domain.reply.ReplyWithPost;
import lombok.Getter;

@Getter
public class ReplyAddEvent {
    private Long postId;
    private String postWriter;
    private Long replyId;
    private String replyWriter;
    private Long parentReplyId;
    private String parentReplyWriter;

    public static ReplyAddEvent from(ReplyWithPost reply, String parentReplyWriter){
        ReplyAddEvent event = new ReplyAddEvent();
        event.postId = reply.getPost().getId();
        event.postWriter = reply.getPost().getCreatedBy();
        event.replyId = reply.getId();
        event.replyWriter = reply.getCreatedBy();
        if(reply.getParentId() != null){
            event.parentReplyId = reply.getParentId();
            event.parentReplyWriter = parentReplyWriter;
        }
        return event;
    }
}