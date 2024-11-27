package com.eighttoten.service.board;

import static com.eighttoten.exception.ExceptionCode.DUPLICATED_BOARD_SCRAP;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_BOARD_SCRAP;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.board.BoardScrap;
import com.eighttoten.domain.member.Member;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.board.BoardScrapRepository;
import com.eighttoten.service.event.board.BoardScrapAddEvent;
import com.eighttoten.service.event.board.BoardScrapSubEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardScrapService {
    private final BoardScrapRepository boardScrapRepository;
    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

    public boolean existsByMemberAndBoardId(Member member, Long boardId) {
        return boardScrapRepository.existsByMemberAndBoardId(member, boardId);
    }

    public BoardScrap findByMemberAndBoardId(Member member, Long boardId) {
        return boardScrapRepository.findByMemberAndBoardId(member, boardId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD_SCRAP));
    }

    public List<BoardScrap> findAllByMemberWithBoard(Member member) {
        return boardScrapRepository.findAllByMemberWithBoard(member);
    }

    @Transactional
    public void add(Member member, Long boardId) {
        boolean hasScrap = existsByMemberAndBoardId(member, boardId);
        if(hasScrap){
            throw new DuplicatedException(DUPLICATED_BOARD_SCRAP);
        }
        Board board = boardService.findById(boardId);
        BoardScrap boardScrap = BoardScrap.of(board, member);
        boardScrapRepository.save(boardScrap);
        publisher.publishEvent(new BoardScrapAddEvent(boardId));
    }

    @Transactional
    public void delete(Member member, Long boardId) {
        BoardScrap boardScrap = findByMemberAndBoardId(member, boardId);
        boardScrapRepository.delete(boardScrap);
        publisher.publishEvent(new BoardScrapSubEvent(boardId));
    }
}
