package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.board.BoardScrapRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardScrapServiceImpl implements BoardScrapService{

    private final BoardScrapRepository boardScrapRepository;

    @Override
    public BoardScrap findByMemberAndBoard(Member member, Board board) {
        return boardScrapRepository.findByMemberAndBoard(member, board)
                .orElseThrow(() -> new EntityNotFoundException("해당 스크랩이 존재하지 않습니다."));
    }

    @Override
    public boolean existsByMemberAndBoard(Member member, Board board) {
        return boardScrapRepository.existsByMemberAndBoard(member, board);
    }

    @Override
    @Transactional
    public void add(Member member, Board board) {
        boolean hasScrap = existsByMemberAndBoard(member, board);
        if(hasScrap){
            throw new RuntimeException("이미 스크랩한 게시글 입니다.");
        }

        BoardScrap boardScrap = BoardScrap.from(board, member);
        board.addScrap();
        boardScrapRepository.save(boardScrap);
    }

    @Override
    public void delete(Member member, Board board) {
        BoardScrap boardScrap = findByMemberAndBoard(member, board);
        board.subScrap();
        boardScrapRepository.delete(boardScrap);
    }
}
