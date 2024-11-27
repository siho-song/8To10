package com.eighttoten.repository.board;

import com.eighttoten.domain.board.BoardScrap;
import com.eighttoten.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardScrapRepository extends JpaRepository<BoardScrap, Long> {
    @Modifying
    @Query("delete from BoardScrap s where s.board.id = :boardId")
    void deleteScrapByBoardId(@Param(value = "boardId") Long boardId);

    boolean existsByMemberAndBoardId(Member member, Long boardId);

    Optional<BoardScrap> findByMemberAndBoardId(Member member, Long boardId);

    @EntityGraph("board")
    @Query("select s from BoardScrap s where s.member = :member")
    List<BoardScrap> findAllByMemberWithBoard(Member member);
}