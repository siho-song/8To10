package com.eighttoten.member.dto.response;

import com.eighttoten.community.domain.Board;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberBoardsResponse {

    private Long boardId;
    private String title;
    private long totalLike;
    private long totalScrap;
    private LocalDateTime createdAt;

    public static MemberBoardsResponse from(Board board){
        MemberBoardsResponse memberBoardsResponse = new MemberBoardsResponse();
        memberBoardsResponse.boardId = board.getId();
        memberBoardsResponse.title = board.getTitle();
        memberBoardsResponse.totalLike = board.getTotalLike();
        memberBoardsResponse.totalScrap = board.getTotalScrap();
        memberBoardsResponse.createdAt = board.getCreatedAt();

        return memberBoardsResponse;
    }
}