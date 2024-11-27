package com.eighttoten.service.board;

import static com.eighttoten.exception.ExceptionCode.DUPLICATED_BOARD_HEART;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_BOARD_HEART;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.board.BoardHeart;
import com.eighttoten.domain.member.Member;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.board.BoardHeartRepository;
import com.eighttoten.service.event.board.BoardHeartAddEvent;
import com.eighttoten.service.event.board.BoardHeartSubEvent;

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
        BoardHeart boardHeart = BoardHeart.of(board, member);

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
