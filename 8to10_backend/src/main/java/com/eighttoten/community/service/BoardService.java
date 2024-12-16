package com.eighttoten.community.service;

import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_BOARD;

import com.eighttoten.community.domain.Board;
import com.eighttoten.community.domain.repository.BoardHeartRepository;
import com.eighttoten.community.domain.repository.BoardRepository;
import com.eighttoten.community.domain.repository.BoardScrapRepository;
import com.eighttoten.community.domain.repository.ReplyHeartRepository;
import com.eighttoten.community.dto.BoardPageRequest;
import com.eighttoten.community.dto.BoardPageResponse;
import com.eighttoten.community.dto.BoardSearchResponse;
import com.eighttoten.community.dto.BoardUpdateRequest;
import com.eighttoten.community.dto.event.BoardEvent;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardScrapRepository boardScrapRepository;
    private final ReplyHeartRepository replyHeartRepository;

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    public Board findByIdWithMember(Long id) {
        return boardRepository.findByIdWithMember(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    public Board findByIdWithRepliesAndMember(Long id) {
        return boardRepository.findByIdWithRepliesAndMember(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    public List<Board> findAllByMember(Member member) {
        return boardRepository.findAllByMember(member);
    }

    public Page<BoardPageResponse> searchBoardPage(BoardPageRequest searchCond, Pageable pageable) {
        return boardRepository.searchPage(searchCond, pageable);
    }

    public BoardSearchResponse searchBoard(Long id, Member member) {
        return boardRepository.searchBoard(id, member)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Board board = findByIdWithRepliesAndMember(id);
        String createdBy = board.getMember().getEmail();
        if (member.isSameEmail(createdBy)) {
            boardHeartRepository.deleteHeartsByBoardId(id);
            boardScrapRepository.deleteScrapByBoardId(id);
            replyHeartRepository.deleteByReplies(board.getReplies());
            boardRepository.deleteById(id);
        }
    }

    @Transactional
    public void update(Member member, BoardUpdateRequest updateRequest) {
        Board board = findByIdWithMember(updateRequest.getId());
        String createdBy = board.getMember().getEmail();
        if (member.isSameEmail(createdBy)) {
            board.update(updateRequest.getTitle(), updateRequest.getContents());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEvent(BoardEvent event) {
        Board board = findById(event.getBoardId());
        event.execute(board);
    }
}