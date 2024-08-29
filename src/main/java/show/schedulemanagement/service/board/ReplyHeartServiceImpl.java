package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.board.reply.ReplyHeart;
import show.schedulemanagement.domain.member.Member;
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

    @Override
    public boolean existsReplyHeartByMemberAndReply(Member member, Reply reply) {
        return replyHeartRepository.existsReplyHeartByMemberAndReply(member, reply);
    }

    @Override
    public ReplyHeart findByMemberAndReply(Member member, Reply reply) {
        return replyHeartRepository.findByMemberAndReply(member,reply).orElseThrow(() -> new EntityNotFoundException("삭제할 좋아요가 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public void add(Reply reply, Member member) {
        boolean hasLiked = existsReplyHeartByMemberAndReply(member, reply);

        if(hasLiked){
            throw new RuntimeException("이미 좋아요 한 댓글 입니다."); //TODO 추후 커스텀 예외 처리
        }
        ReplyHeart replyHeart = ReplyHeart.createReplyHeart(reply, member);
        reply.addLike();
        replyHeartRepository.save(replyHeart);
    }

    @Override
    public void delete(Reply reply, Member member) {
        ReplyHeart replyHeart = findByMemberAndReply(member, reply);

        reply.subLike();
        replyHeartRepository.delete(replyHeart);
    }
}
