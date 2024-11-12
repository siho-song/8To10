package show.schedulemanagement.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.service.board.BoardService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {

    private final BoardService boardService;

    public ProfileResponse getProfile(Member member) {
        return ProfileResponse.of(member);
    }

    public Result<MemberBoardsResponse> getMemberBoards(Member member) {
        List<Board> boards = boardService.findAllByMember(member);
        return Result.fromElements(boards, MemberBoardsResponse::of);
    }

}