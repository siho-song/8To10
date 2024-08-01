package show.schedulemanagement.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
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

    @PostMapping("/add")
    public ResponseEntity<BoardResponseDto> save(@RequestBody BoardRequestDto requestDto){
        Member member = memberService.getAuthenticatedMember();
        Board board = Board.from(member,requestDto);

        boardService.save(board);
        BoardResponseDto responseDto = BoardResponseDto.from(board);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
