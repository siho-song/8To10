package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.repository.board.reply.ReplyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public Reply findById(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
    }
}
