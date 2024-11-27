package show.schedulemanagement.service.event.reply;

import show.schedulemanagement.domain.board.reply.Reply;

public class ReplyHeartSubEvent extends ReplyEvent {

    public ReplyHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Reply reply) {
        reply.subLike();
    }
}
