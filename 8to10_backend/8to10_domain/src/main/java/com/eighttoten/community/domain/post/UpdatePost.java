package com.eighttoten.community.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePost {
    private final Long id;
    private final String title;
    private final String contents;
}