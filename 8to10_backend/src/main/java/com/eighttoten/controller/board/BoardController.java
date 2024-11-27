package com.eighttoten.controller.board;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.board.BoardPageRequest;
import com.eighttoten.dto.board.BoardPageResponse;
import com.eighttoten.dto.board.BoardSaveRequest;
import com.eighttoten.dto.board.BoardSaveResponse;
import com.eighttoten.dto.board.BoardSearchResponse;
import com.eighttoten.dto.board.BoardUpdateRequest;
import com.eighttoten.service.board.BoardHeartService;
import com.eighttoten.service.board.BoardScrapService;
import com.eighttoten.service.board.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final BoardHeartService boardHeartService;
    private final BoardScrapService boardScrapService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BoardPageResponse>> getBoardPage(@ModelAttribute BoardPageRequest searchCond) {
        Pageable pageable = PageRequest.of(searchCond.getPageNum(), searchCond.getPageSize());
        Page<BoardPageResponse> result = boardService.searchBoardPage(searchCond, pageable);
        return new ResponseEntity<>(result, OK);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardSaveResponse> updateBoard(
            @CurrentMember Member member,
            @RequestBody BoardUpdateRequest request) {

        boardService.update(member, request);
        Board board = boardService.findByIdWithMember(request.getId());
        return new ResponseEntity<>(BoardSaveResponse.from(board), OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardSearchResponse> getBoard(
            @CurrentMember Member member,
            @PathVariable(name = "id") Long id) {

        BoardSearchResponse board = boardService.searchBoard(id, member);
        return new ResponseEntity<>(board, OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteBoard(
            @CurrentMember Member member,
            @PathVariable(name = "id") Long id) {

        boardService.deleteById(member, id);
        return new ResponseEntity<>(id, OK);
    }

    @PostMapping(value = "/add", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardSaveResponse> save(
            @CurrentMember Member member,
            @RequestBody @Valid BoardSaveRequest requestDto) {

        Board board = Board.from(member, requestDto);

        boardService.save(board);
        BoardSaveResponse responseDto = BoardSaveResponse.from(board);

        return new ResponseEntity<>(responseDto, CREATED);
    }

    @PostMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long boardId) {

        boardHeartService.add(boardId, member);
        return new ResponseEntity<>(boardId, CREATED);
    }

    @DeleteMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long boardId) {

        boardHeartService.delete(boardId, member);
        return new ResponseEntity<>(boardId, OK);
    }

    @PostMapping(value = "/{id}/scrap", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addScrap(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long boardId) {

        boardScrapService.add(member, boardId);
        return new ResponseEntity<>(boardId, CREATED);
    }

    @DeleteMapping(value = "/{id}/scrap", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteScrap(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long boardId) {

        boardScrapService.delete(member, boardId);
        return new ResponseEntity<>(boardId, OK);
    }
}