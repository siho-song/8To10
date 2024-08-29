package show.schedulemanagement.service.board;

import java.util.List;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;

public interface ReplyHeartService {
    void deleteByReply(Reply reply);
    void deleteByReplies(List<Reply> replies);
    void addHeart(Reply reply, Member member);
}
