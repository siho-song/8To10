package show.schedulemanagement.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHearts;

public interface BoardHeartsRepository extends JpaRepository<BoardHearts, Long> {
    @Modifying
    @Query("delete from BoardHearts h where h.board = :board")
    void deleteHeartsByBoard(@Param(value = "board") Board board);
}
