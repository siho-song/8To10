package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;

public interface BoardScrapService {
    boolean existsByMemberAndBoard(Member member, Board board);
    void add(Member member, Board board);
}
