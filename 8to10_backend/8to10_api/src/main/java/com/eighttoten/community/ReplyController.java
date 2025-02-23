package com.eighttoten.community;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eighttoten.community.domain.reply.NewReplyHeart;
import com.eighttoten.community.dto.reply.AddReplyRequest;
import com.eighttoten.community.dto.reply.ReplyUpdateRequest;
import com.eighttoten.community.service.ReplyHeartService;
import com.eighttoten.community.service.ReplyService;
import com.eighttoten.member.domain.Member;
import com.eighttoten.support.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    private final ReplyHeartService replyHeartService;

    @PostMapping(value = "/save", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(
            @CurrentMember Member member,
            @RequestBody @Valid AddReplyRequest request)
    {
        replyService.addReply(request.toNewReply(member.getId()));
        return ResponseEntity.status(CREATED).build();
    }

    @PutMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @CurrentMember Member member,
            @RequestBody @Valid ReplyUpdateRequest request)
    {
        replyService.update(member, request.toReplyUpdate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        replyService.deleteById(member,id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id)
    {
        replyHeartService.addReplyHeart(NewReplyHeart.from(member.getId(),id));
        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping(value = "/{id}/heart", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteHeart(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long replyId)
    {
        replyHeartService.deleteByMemberIdAndReplyId(member.getId(), replyId);
        return ResponseEntity.noContent().build();
    }
}