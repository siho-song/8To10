package show.schedulemanagement.service.board.reply;

import java.util.List;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.board.reply.ReplyHeart;
import show.schedulemanagement.domain.member.Member;

public interface ReplyHeartService {
    boolean existsReplyHeartByMemberAndReplyId(Member member, Long replyId);
    ReplyHeart findByMemberAndReplyId(Member member, Long replyId);
    void add(Long replyId, Member member);
    void delete(Long replyId, Member member);
}
