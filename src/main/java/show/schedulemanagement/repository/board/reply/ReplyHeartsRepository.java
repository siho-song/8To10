package show.schedulemanagement.repository.board.reply;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.board.reply.ReplyHearts;

public interface ReplyHeartsRepository extends JpaRepository<ReplyHearts, Long> {

    @Modifying
    @Query("delete from ReplyHearts rh where rh.reply in :replies")
    void deleteByReplies(@Param(value = "replies") List<Reply> replies);

    @Modifying
    @Query("delete from ReplyHearts rh where rh.reply = :reply")
    void deleteByReply(@Param(value = "reply") Reply reply);
}
