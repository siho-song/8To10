package show.schedulemanagement.repository.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.member.Member;

public interface BoardHeartRepository extends JpaRepository<BoardHeart, Long> {
    @Modifying
    @Query("delete from BoardHeart h where h.board.id = :boardId")
    void deleteHeartsByBoardId(@Param(value = "boardId") Long boardId);
    Optional<BoardHeart> findByMemberAndBoardId(Member member, Long boardId);
    boolean existsBoardHeartByMemberAndBoardId(Member member, Long boardId);
}
