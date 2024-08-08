package show.schedulemanagement.controller.board;

import static org.springframework.http.MediaType.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardSearchCond;
import show.schedulemanagement.dto.board.BoardRequestDto;
import show.schedulemanagement.dto.board.BoardResponseDto;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.board.BoardService;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping(value = "/add" , consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponseDto> save(@RequestBody BoardRequestDto requestDto){
        Member member = memberService.getAuthenticatedMember();
        Board board = Board.from(member,requestDto);

        boardService.save(board);
        BoardResponseDto responseDto = BoardResponseDto.from(board);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BoardResponseDto>> getBoardPage(@RequestBody BoardSearchCond searchCond) {
        Pageable pageable = PageRequest.of(searchCond.getPageNum(), searchCond.getPageSize());
        Page<BoardResponseDto> result = boardService.searchBoardPage(searchCond, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable(name = "id") Long id){
        Board board = boardService.findById(id);
        return new ResponseEntity<>(BoardResponseDto.from(board), HttpStatus.OK);
    }
}
