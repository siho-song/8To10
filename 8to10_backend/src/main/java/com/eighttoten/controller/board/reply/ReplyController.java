package com.eighttoten.controller.board.reply;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eighttoten.config.web.CurrentMember;
import com.eighttoten.domain.board.reply.Reply;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.board.reply.ReplySaveRequest;
import com.eighttoten.dto.board.reply.ReplySaveResponse;
import com.eighttoten.dto.board.reply.ReplySearchResponse;
import com.eighttoten.dto.board.reply.ReplyUpdateRequest;
import com.eighttoten.service.board.reply.ReplyHeartService;
import com.eighttoten.service.board.reply.ReplyService;

@RestController
@RequestMapping("/community/reply")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {

    private final ReplyService replyService;
    private final ReplyHeartService replyHeartService;

    @PutMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplySearchResponse> update(
            @CurrentMember Member member,
            @RequestBody @Valid ReplyUpdateRequest request)
    {
        replyService.update(member, request);
        Reply reply = replyService.findByIdWithMemberAndParent(request.getId());
        ReplySearchResponse responseDto = ReplySearchResponse.from(reply);
        return new ResponseEntity<>(responseDto, OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> delete(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        replyService.delete(member,id);
        return new ResponseEntity<>(id, OK);
    }

    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplySaveResponse> save(
            @CurrentMember Member member,
            @RequestBody @Valid ReplySaveRequest request)
    {
        Reply reply = replyService.save(request, member);
        ReplySaveResponse responseDto = ReplySaveResponse.from(reply);
        return new ResponseEntity<>(responseDto, CREATED);
    }

    @PostMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        replyHeartService.add(id, member);
        return new ResponseEntity<>(id, CREATED);
    }

    @DeleteMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        replyHeartService.delete(id, member);
        return new ResponseEntity<>(id, OK);
    }
}
