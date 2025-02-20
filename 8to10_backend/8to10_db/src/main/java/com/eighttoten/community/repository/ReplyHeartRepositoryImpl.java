package com.eighttoten.community.repository;

import com.eighttoten.community.ReplyEntity;
import com.eighttoten.community.ReplyHeartEntity;
import com.eighttoten.community.domain.reply.NewReplyHeart;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyHeartRepositoryImpl implements ReplyHeartRepository {
    private final ReplyHeartJpaRepository replyHeartRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ReplyJpaRepository replyJpaRepository;

    @Override
    public void save(NewReplyHeart newReplyHeart) {
        MemberEntity memberEntity = memberJpaRepository.findById(newReplyHeart.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

        ReplyEntity replyEntity = replyJpaRepository.findById(newReplyHeart.getReplyId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_REPLY));
        replyHeartRepository.save(ReplyHeartEntity.from(memberEntity, replyEntity));
    }

    @Override
    public void deleteAllByReplyId(Long replyId) {
        replyHeartRepository.deleteAllByReplyId(replyId);
    }

    @Override
    public void deleteAllByReplyIds(List<Long> replyIds) {
        replyHeartRepository.deleteAllByReplyIds(replyIds);
    }

    @Override
    public void deleteByMemberIdAndReplyId(Long memberId, Long replyId) {
        replyHeartRepository.deleteByMemberEntityIdAndReplyEntityId(memberId, replyId);
    }

    public boolean existsByMemberIdAndReplyId(Long memberId, Long replyId) {
        return replyHeartRepository.existsByMemberEntityIdAndReplyEntityId(memberId, replyId);
    }
}
