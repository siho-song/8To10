package com.eighttoten.community.domain.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewReply {
    private Long memberId;
    private Long postId;
    private Long parentId;
    private String contents;
}
