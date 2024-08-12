package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.board.reply.Reply;

public interface ReplyService {
    Reply findById(Long id);
    Reply findByIdWithParent(Long id);
}
