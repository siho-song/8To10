package show.schedulemanagement.repository.board;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.repository.schedule.ScheduleRepositoryCustom;

public interface BoardRepository extends JpaRepository<Board, Long> , BoardRepositoryCustom{
    @EntityGraph(attributePaths = {"member", "replies"})
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByIdWithReplies(@Param(value = "id") Long id);
}
