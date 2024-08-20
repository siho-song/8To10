package show.schedulemanagement.controller.board.reply;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.reply.ReplySaveRequest;
import show.schedulemanagement.dto.board.reply.ReplySaveResponse;
import show.schedulemanagement.dto.board.reply.ReplySearchResponse;
import show.schedulemanagement.dto.board.reply.ReplyUpdateRequest;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.board.ReplyService;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {

    private final ReplyService replyService;
    private final MemberService memberService;

    @PatchMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplySearchResponse> update(@RequestBody @Valid ReplyUpdateRequest request){
        Member member = memberService.getAuthenticatedMember();
        replyService.update(member, request);
        Reply reply = replyService.findByIdWithMemberAndParent(request.getId());
        ReplySearchResponse responseDto = ReplySearchResponse.from(reply);
        return new ResponseEntity<>(responseDto, OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> delete(@PathVariable(value = "id") Long id){
        Member member = memberService.getAuthenticatedMember();
        replyService.delete(member,id);
        return new ResponseEntity<>(id, OK);
    }

    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplySaveResponse> save(@RequestBody @Valid ReplySaveRequest request) {
        Member member = memberService.getAuthenticatedMember();
        Reply reply = replyService.save(request, member);
        ReplySaveResponse responseDto = ReplySaveResponse.from(reply);
        return new ResponseEntity<>(responseDto, CREATED);
    }
}
