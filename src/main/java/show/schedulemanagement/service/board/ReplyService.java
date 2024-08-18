package show.schedulemanagement.service.board;

import java.util.List;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.reply.ReplySaveRequest;

public interface ReplyService {
    Reply findById(Long id);
    Reply findByIdWithParent(Long id);
    Reply findByIdWithMemberAndParent(Long id);
    List<Reply> findAllWithBoardAndMemberByEmail(Member member);
    List<Reply> findNestedRepliesByParent(Reply reply);
    void deleteByReplies(List<Reply> replies);
    Reply save(ReplySaveRequest request, Member member);
}
