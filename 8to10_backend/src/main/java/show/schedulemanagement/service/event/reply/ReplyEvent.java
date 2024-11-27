package show.schedulemanagement.service.event.reply;

import lombok.Getter;
import show.schedulemanagement.domain.board.reply.Reply;

@Getter
public abstract class ReplyEvent {
    protected final Long replyId;

    public ReplyEvent(Long id) {
        this.replyId = id;
    }

    public abstract void execute(Reply reply);
}
