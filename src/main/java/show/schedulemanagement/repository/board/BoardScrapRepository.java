package show.schedulemanagement.repository.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;

public interface BoardScrapRepository extends JpaRepository<BoardScrap, Long> {

    Optional<BoardScrap> findByMemberAndBoard(Member member, Board board);

    @Modifying
    @Query("delete from BoardScrap s where s.board = :board")
    void deleteScrapByBoard(@Param(value = "board") Board board);

    boolean existsByMemberAndBoard(Member member, Board board);
}
