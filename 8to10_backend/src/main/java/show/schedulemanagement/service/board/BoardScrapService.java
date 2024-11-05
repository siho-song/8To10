package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;

public interface BoardScrapService {
    BoardScrap findByMemberAndBoardId(Member member, Long boardId);
    boolean existsByMemberAndBoardId(Member member, Long boardId);
    void add(Member member, Long boardId);
    void delete(Member member, Long boardId);
}
