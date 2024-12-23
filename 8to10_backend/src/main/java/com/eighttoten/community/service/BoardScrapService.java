package com.eighttoten.community.service;

import static com.eighttoten.global.exception.ExceptionCode.DUPLICATED_BOARD_SCRAP;
import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_BOARD_SCRAP;

import com.eighttoten.community.domain.Board;
import com.eighttoten.community.domain.BoardScrap;
import com.eighttoten.community.domain.repository.BoardScrapRepository;
import com.eighttoten.community.dto.event.BoardScrapAddEvent;
import com.eighttoten.community.dto.event.BoardScrapSubEvent;
import com.eighttoten.global.exception.DuplicatedException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
