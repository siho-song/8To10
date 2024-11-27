package show.schedulemanagement.service.event.reply;

import show.schedulemanagement.domain.board.reply.Reply;

public class ReplyHeartAddEvent extends ReplyEvent {

    public ReplyHeartAddEvent(Long replyId) {
        super(replyId);
    }

    @Override
    public void execute(Reply reply) {
        reply.addLike();
    }
}
