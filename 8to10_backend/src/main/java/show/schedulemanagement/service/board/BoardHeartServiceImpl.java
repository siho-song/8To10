package show.schedulemanagement.service.board;

import static show.schedulemanagement.exception.ExceptionCode.DUPLICATED_BOARD_HEART;
import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_BOARD_HEART;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.exception.DuplicatedException;
import show.schedulemanagement.exception.NotFoundEntityException;
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
            throw new DuplicatedException(DUPLICATED_BOARD_HEART);
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
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD_HEART));

        boardHeartRepository.delete(boardHeart);
        publisher.publishEvent(new BoardHeartSubEvent(boardId));
    }
}
