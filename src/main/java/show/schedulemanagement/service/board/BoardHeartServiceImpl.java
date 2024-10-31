package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.board.BoardHeartRepository;
import show.schedulemanagement.service.event.board.BoardHeartAddEvent;
import show.schedulemanagement.service.event.board.BoardHeartSubEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardHeartServiceImpl implements BoardHeartService{

    private final BoardHeartRepository boardHeartRepository;
    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void add(Long boardId, Member member) {
        boolean hasLiked = boardHeartRepository.existsBoardHeartByMemberAndBoardId(member, boardId);

        if(hasLiked){
            throw new RuntimeException("이미 좋아요 한 게시글 입니다."); //TODO 추후 커스텀 예외 처리
        }

        Board board = boardService.findById(boardId);
        BoardHeart boardHeart = BoardHeart.createBoardHeart(board, member);

        boardHeartRepository.save(boardHeart);
        publisher.publishEvent(new BoardHeartAddEvent(boardId));
    }

    @Override
    @Transactional
    public void delete(Long boardId, Member member) {
        BoardHeart boardHeart = boardHeartRepository.findByMemberAndBoardId(member,boardId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 좋아요가 존재하지 않습니다."));

        boardHeartRepository.delete(boardHeart);
        publisher.publishEvent(new BoardHeartSubEvent(boardId));
    }
}
