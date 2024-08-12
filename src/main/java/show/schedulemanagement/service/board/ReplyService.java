package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.reply.ReplySaveRequest;

public interface ReplyService {
    Reply findById(Long id);
    Reply findByIdWithParent(Long id);
    Reply save(ReplySaveRequest request, Member member);
}
