package show.schedulemanagement.service;

import static show.schedulemanagement.exception.ExceptionCode.REQUIRE_CONTENT_TYPE;
import static show.schedulemanagement.exception.ExceptionCode.REQUIRE_IMAGE_FILE;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import show.schedulemanagement.constant.AppConstant;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.MemberRepliesResponse;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.dto.mypage.ScrappedBoardResponse;
import show.schedulemanagement.exception.BadRequestException;
import show.schedulemanagement.service.board.BoardScrapService;
import show.schedulemanagement.service.board.BoardService;
import show.schedulemanagement.service.board.reply.ReplyService;
import show.schedulemanagement.service.member.MemberService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {

    private final BoardService boardService;
    private final BoardScrapService boardScrapService;
    private final ReplyService replyService;
    private final MemberService memberService;
    private final MultipartFileStorageService multiPartFileStorageService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ProfileResponse getProfile(Member member) {
        return ProfileResponse.from(member);
    }

    public Result<MemberBoardsResponse> getMemberBoards(Member member) {
        List<Board> boards = boardService.findAllByMember(member);
        return Result.fromElements(boards, MemberBoardsResponse::from);
    }

    public Result<MemberRepliesResponse> getMemberReplies(Member member) {
        List<Reply> replies = replyService.findAllByMemberWithBoard(member);
        return Result.fromElements(replies, MemberRepliesResponse::from);
    }

    public Result<ScrappedBoardResponse> getScrappedBoard(Member member) {
        List<BoardScrap> boardScraps = boardScrapService.findAllByMemberWithBoard(member);
        return Result.fromElements(boardScraps, boardScrap -> ScrappedBoardResponse.from(boardScrap.getBoard()));
    }

    @Transactional
    public void updateNickname(String nickname, Long memberId) {
        Member member = memberService.findById(memberId);
        member.updateNickname(nickname);
    }

    @Transactional
    public void updatePassword(String password, Long memberId) {
        Member member = memberService.findById(memberId);
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        member.updatePassword(encodedPassword);
    }

    @Transactional
    public void uploadProfilePhoto(Member member, MultipartFile file) throws IOException {
        validateImageFileType(file.getContentType());
        Member findMember = memberService.findById(member.getId());

        String profileImageUrl = findMember.getImageFile();
        if(profileImageUrl != null){
            multiPartFileStorageService.deleteFile(profileImageUrl);
        }

        String savedFilePath = multiPartFileStorageService.saveFile(file, AppConstant.imageFilePath);
        findMember.updateProfilePhoto(savedFilePath);
    }

    @Transactional
    public void deleteProfilePhoto(Member member) {
        Member findMember = memberService.findById(member.getId());
        String imageFileUrl = findMember.getImageFile();
        if(imageFileUrl != null){
            multiPartFileStorageService.deleteFile(imageFileUrl);
            findMember.updateProfilePhoto(null);
        }
    }

    private void validateImageFileType(String contentType) {
        if(contentType == null) {
            throw new BadRequestException(REQUIRE_CONTENT_TYPE);
        }

        if (!contentType.startsWith("image/")) {
            throw new BadRequestException(REQUIRE_IMAGE_FILE);
        }
    }
}