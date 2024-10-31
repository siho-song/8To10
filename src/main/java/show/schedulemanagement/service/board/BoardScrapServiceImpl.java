package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.board.BoardScrapRepository;
import show.schedulemanagement.service.event.board.BoardScrapAddEvent;
import show.schedulemanagement.service.event.board.BoardScrapSubEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardScrapServiceImpl implements BoardScrapService{

    private final BoardScrapRepository boardScrapRepository;
    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

    @Override
    public BoardScrap findByMemberAndBoardId(Member member, Long boardId) {
        return boardScrapRepository.findByMemberAndBoardId(member, boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 스크랩이 존재하지 않습니다."));
    }

    @Override
    public boolean existsByMemberAndBoardId(Member member, Long boardId) {
        return boardScrapRepository.existsByMemberAndBoardId(member, boardId);
    }

    @Override
    @Transactional
    public void add(Member member, Long boardId) {
        boolean hasScrap = existsByMemberAndBoardId(member, boardId);
        if(hasScrap){
            throw new RuntimeException("이미 스크랩한 게시글 입니다.");
        }
        Board board = boardService.findById(boardId);
        BoardScrap boardScrap = BoardScrap.from(board, member);
        boardScrapRepository.save(boardScrap);
        publisher.publishEvent(new BoardScrapAddEvent(boardId));
    }

    @Override
    @Transactional
    public void delete(Member member, Long boardId) {
        BoardScrap boardScrap = findByMemberAndBoardId(member, boardId);
        boardScrapRepository.delete(boardScrap);
        publisher.publishEvent(new BoardScrapSubEvent(boardId));
    }
}
