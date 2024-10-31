package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.member.Member;

public interface BoardHeartService {
    void add(Long boardId, Member member);
    void delete(Long boardId, Member member);
}
