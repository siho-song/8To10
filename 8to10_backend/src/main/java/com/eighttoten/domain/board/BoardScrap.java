package com.eighttoten.domain.board;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class BoardScrap{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_scrap_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static BoardScrap of(Board board, Member member) {
        BoardScrap boardScrap = new BoardScrap();
        boardScrap.board = board;
        boardScrap.member = member;
        return boardScrap;
    }
}