package com.eighttoten.community.service;

import static com.eighttoten.global.exception.ExceptionCode.DUPLICATED_BOARD_HEART;
import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_BOARD_HEART;

import com.eighttoten.community.domain.Board;
import com.eighttoten.community.domain.BoardHeart;
import com.eighttoten.community.domain.repository.BoardHeartRepository;
import com.eighttoten.community.dto.event.BoardHeartAddEvent;
import com.eighttoten.community.dto.event.BoardHeartSubEvent;
import com.eighttoten.global.exception.DuplicatedException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardHeartService {
    private final BoardHeartRepository boardHeartRepository;
    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

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

    @Transactional
    public void delete(Long boardId, Member member) {
        BoardHeart boardHeart = boardHeartRepository.findByMemberAndBoardId(member,boardId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD_HEART));

        boardHeartRepository.delete(boardHeart);
        publisher.publishEvent(new BoardHeartSubEvent(boardId));
    }
}