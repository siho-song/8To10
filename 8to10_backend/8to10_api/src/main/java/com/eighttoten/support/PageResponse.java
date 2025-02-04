package com.eighttoten.support;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PageResponse<T> {
    private List<T> contents;
    private long pageNumber;
    private long pageSize;
    private long totalPages;
    private long totalElements;
}