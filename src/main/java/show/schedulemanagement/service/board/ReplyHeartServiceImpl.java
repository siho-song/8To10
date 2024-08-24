package show.schedulemanagement.service.board;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.repository.board.reply.ReplyHeartRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReplyHeartServiceImpl implements ReplyHeartService {

    private final ReplyHeartRepository replyHeartRepository;

    @Override
    @Transactional
    public void deleteByReply(Reply reply) {
        replyHeartRepository.deleteByReply(reply);
    }

    @Override
    @Transactional
    public void deleteByReplies(List<Reply> replies) {
        replyHeartRepository.deleteByReplies(replies);
    }
}
