package show.schedulemanagement.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.board.BoardScrapRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardScrapServiceImpl implements BoardScrapService{

    private final BoardScrapRepository boardScrapRepository;
    
    @Override
    public boolean existsByMemberAndBoard(Member member, Board board) {
        return boardScrapRepository.existsByMemberAndBoard(member, board);
    }

}
