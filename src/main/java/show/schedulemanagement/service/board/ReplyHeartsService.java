package show.schedulemanagement.service.board;

import java.util.List;
import show.schedulemanagement.domain.board.reply.Reply;

public interface ReplyHeartsService {
    void deleteByReply(Reply reply);
    void deleteByReplies(List<Reply> replies);
}
