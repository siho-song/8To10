package show.schedulemanagement.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;

public interface BoardScrapRepository extends JpaRepository<BoardScrap, Long> {
    @Modifying
    @Query("delete from BoardScrap s where s.board = :board")
    void deleteScrapByBoard(@Param(value = "board") Board board);
}
