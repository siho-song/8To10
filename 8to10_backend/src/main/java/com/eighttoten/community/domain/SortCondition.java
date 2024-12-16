package com.eighttoten.community.domain;

import lombok.Getter;

@Getter
public enum SortCondition {
    LIKE("좋아요순"),SCRAP("스크랩순"),DATE("날짜순");

    private final String value;

    SortCondition(String value) {
        this.value = value;
    }
}