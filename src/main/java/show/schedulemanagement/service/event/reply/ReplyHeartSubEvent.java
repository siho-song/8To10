package show.schedulemanagement.service.event.reply;

import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.service.event.board.BoardEvent;

public class ReplyHeartSubEvent extends ReplyEvent {

    public ReplyHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Reply reply) {
        reply.subLike();
    }
}
