package show.schedulemanagement.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.MemberRepliesResponse;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.dto.mypage.ScrappedBoardResponse;
import show.schedulemanagement.service.board.BoardScrapService;
import show.schedulemanagement.service.board.BoardService;
import show.schedulemanagement.service.board.reply.ReplyService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {

    private final BoardService boardService;
    private final BoardScrapService boardScrapService;
    private final ReplyService replyService;

    public ProfileResponse getProfile(Member member) {
        return ProfileResponse.of(member);
    }

    public Result<MemberBoardsResponse> getMemberBoards(Member member) {
        List<Board> boards = boardService.findAllByMember(member);
        return Result.fromElements(boards, MemberBoardsResponse::of);
    }

    public Result<MemberRepliesResponse> getMemberReplies(Member member) {
        List<Reply> replies = replyService.findAllByMemberWithBoard(member);
        return Result.fromElements(replies, MemberRepliesResponse::of);
    }

    public Result<ScrappedBoardResponse> getScrappedBoard(Member member) {
        List<BoardScrap> boardScraps = boardScrapService.findAllByMemberWithBoard(member);
        return Result.fromElements(boardScraps, boardScrap -> ScrappedBoardResponse.from(boardScrap.getBoard()));
    }
}