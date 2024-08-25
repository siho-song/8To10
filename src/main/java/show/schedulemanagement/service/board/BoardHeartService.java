package show.schedulemanagement.service.board;

import show.schedulemanagement.domain.member.Member;

public interface BoardHeartService {
    void addHeart(Long boardId, Member member);
}
