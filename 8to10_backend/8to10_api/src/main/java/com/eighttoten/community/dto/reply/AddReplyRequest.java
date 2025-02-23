package com.eighttoten.community.dto.reply;

import com.eighttoten.community.domain.reply.NewReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddReplyRequest {
    @NotNull
    private Long postId;

    private Long parentId;

    @NotBlank
    @Size(min = 2)
    private String contents;

    public NewReply toNewReply(Long memberId){
        return new NewReply(memberId, postId, parentId, contents);
    }
}