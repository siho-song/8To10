package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;

public interface BoardHeartService {
    void add(Board board, Member member);
    void delete(Board board, Member member);
    void deleteHeartsByBoard(Board board);

}
