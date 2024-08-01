package show.schedulemanagement.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.repository.schedule.ScheduleRepositoryCustom;

public interface BoardRepository extends JpaRepository<Board, Long>{
}
