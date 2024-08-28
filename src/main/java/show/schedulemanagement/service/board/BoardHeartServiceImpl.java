package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.board.BoardHeartRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardHeartServiceImpl implements BoardHeartService{

    private final BoardService boardService;
    private final BoardHeartRepository boardHeartRepository;

    @Override
    @Transactional
    public void addHeart(Long boardId, Member member) {
        Board board = boardService.findById(boardId);
        boolean hasLiked = boardHeartRepository.existsBoardHeartByMemberAndBoard(member, board);

        if(hasLiked){
            throw new RuntimeException("이미 좋아요 한 게시글 입니다."); //TODO 추후 커스텀 예외 처리
        }
        BoardHeart boardHeart = BoardHeart.createBoardHeart(board, member);
        board.addLike();
        boardHeartRepository.save(boardHeart);
    }

    @Override
    @Transactional
    public void deleteHeart(Long boardId, Member member) {
        Board board = boardService.findById(boardId);

        BoardHeart boardHeart = boardHeartRepository.findByMemberAndBoard(member,board).orElseThrow(
                () -> new EntityNotFoundException("삭제할 좋아요가 존재하지 않습니다."));

        board.subLike();
        boardHeartRepository.delete(boardHeart);
    }
}
