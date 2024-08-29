package show.schedulemanagement.service.board;

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

}
