package com.eighttoten.dto.board;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardPageRequest {
    private String keyword; //제목, 내용으로 검색
    @Size(min = 1)
    private Integer pageNum = 0;
    private Integer pageSize = 10;
    private SearchCond searchCond;
    private SortCondition sortCond = SortCondition.DATE; //DATE,LIKE,SCRAP
}
