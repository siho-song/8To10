package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;

public interface BoardScrapService {
    BoardScrap findByMemberAndBoard(Member member, Board board);
    boolean existsByMemberAndBoard(Member member, Board board);
    void add(Member member, Board board);
    void delete(Member member, Board board);
}
