package com.eighttoten.service.board.reply;

import java.util.List;
import com.eighttoten.domain.board.reply.Reply;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.board.reply.ReplySaveRequest;
import com.eighttoten.dto.board.reply.ReplyUpdateRequest;

public interface ReplyService {
    Reply findById(Long id);
    Reply findByIdWithParent(Long id);
    Reply findByIdWithMemberAndParent(Long id);
    List<Reply> findAllByMemberWithBoard(Member member);
    List<Reply> findAllWithBoardAndMemberByEmail(Member member);
    List<Reply> findNestedRepliesByParent(Reply reply);
    void deleteByReplies(List<Reply> replies);
    Reply save(ReplySaveRequest request, Member member);
    void delete(Member member, Long id);
    void update(Member member, ReplyUpdateRequest updateRequest);
}
