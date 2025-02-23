package com.eighttoten.community.dto.reply;

import com.eighttoten.community.domain.reply.UpdateReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReplyUpdateRequest {
    @NotNull
    Long id;

    @NotBlank
    @Size(min = 2)
    String contents;

    public UpdateReply toReplyUpdate(){
        return new UpdateReply(id, contents);
    }
}