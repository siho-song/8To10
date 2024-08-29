package show.schedulemanagement.service.board;

import java.util.List;
import java.util.Optional;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.board.reply.ReplyHeart;
import show.schedulemanagement.domain.member.Member;

public interface ReplyHeartService {
    void deleteByReply(Reply reply);
    void deleteByReplies(List<Reply> replies);
    boolean existsReplyHeartByMemberAndReply(Member member, Reply reply);
    ReplyHeart findByMemberAndReply(Member member, Reply reply);
    void add(Reply reply, Member member);
    void delete(Reply reply, Member member);
}
