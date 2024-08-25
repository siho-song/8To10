package show.schedulemanagement.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.member.Member;

public interface BoardHeartRepository extends JpaRepository<BoardHeart, Long> {
    @Modifying
    @Query("delete from BoardHeart h where h.board = :board")
    void deleteHeartsByBoard(@Param(value = "board") Board board);

    boolean existsBoardHeartByMemberAndBoard(Member member, Board board);
}
