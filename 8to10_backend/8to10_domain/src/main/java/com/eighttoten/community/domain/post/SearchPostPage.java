package com.eighttoten.community.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchPostPage {
    private String keyword;
    private String searchCond;
    private String sortCond;
    private String sortDirection;
    private long pageNum;
    private long pageSize;

    public long getOffset(){
        return (pageNum - 1) * pageSize;
    }
}
