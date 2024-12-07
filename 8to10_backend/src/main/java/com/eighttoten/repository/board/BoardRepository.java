package com.eighttoten.repository.board;

import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> , BoardRepositoryCustom{
    @EntityGraph(attributePaths = {"member"})
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByIdWithMember(@Param(value = "id") Long id);
    
    @EntityGraph(attributePaths = {"member", "replies"})
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByIdWithRepliesAndMember(@Param(value = "id") Long id);

    List<Board> findAllByMember(Member member);
}