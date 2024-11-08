package show.schedulemanagement.service.board.reply;

import static show.schedulemanagement.exception.ExceptionCode.DUPLICATED_REPLY_HEART;
import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_REPLY_HEART;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.board.reply.ReplyHeart;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.exception.DuplicatedException;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.board.reply.ReplyHeartRepository;
import show.schedulemanagement.service.event.reply.ReplyHeartAddEvent;
import show.schedulemanagement.service.event.reply.ReplyHeartSubEvent;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReplyHeartServiceImpl implements ReplyHeartService {

    private final ReplyHeartRepository replyHeartRepository;
    private final ReplyService replyService;
    private final ApplicationEventPublisher publisher;

    @Override
    public boolean existsReplyHeartByMemberAndReplyId(Member member, Long replyId) {
        return replyHeartRepository.existsReplyHeartByMemberAndReplyId(member, replyId);
    }

    @Override
    public ReplyHeart findByMemberAndReplyId(Member member, Long replyId) {
        return replyHeartRepository.findByMemberAndReplyId(member, replyId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY_HEART));
    }

    @Override
    @Transactional
    public void add(Long replyId, Member member) {
        boolean hasLiked = existsReplyHeartByMemberAndReplyId(member, replyId);

        if(hasLiked){
            throw new DuplicatedException(DUPLICATED_REPLY_HEART);
        }
        Reply reply = replyService.findById(replyId);
        ReplyHeart replyHeart = ReplyHeart.createReplyHeart(reply, member);

        replyHeartRepository.save(replyHeart);
        publisher.publishEvent(new ReplyHeartAddEvent(replyId));
    }

    @Override
    @Transactional
    public void delete(Long replyId, Member member) {
        ReplyHeart replyHeart = findByMemberAndReplyId(member, replyId);

        replyHeartRepository.delete(replyHeart);
        publisher.publishEvent(new ReplyHeartSubEvent(replyId));
    }
}
