package show.schedulemanagement.repository.board.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.board.reply.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
