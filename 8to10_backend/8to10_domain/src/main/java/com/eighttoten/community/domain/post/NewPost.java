package com.eighttoten.community.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewPost {
    private String title;
    private String contents;
    private Long memberId;
}
