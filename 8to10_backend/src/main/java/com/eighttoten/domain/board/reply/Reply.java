package com.eighttoten.domain.board.reply;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.eighttoten.domain.auditing.baseentity.BaseTimeEntity;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.member.Member;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Table(name = "REPLY")
@ToString(exclude = "parent")
public class Reply extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    private long totalLike;

    public static Reply from(Reply parent, String contents, Member member, Board board) {
        return Reply.builder()
                .member(member)
                .board(board)
                .parent(parent)
                .contents(contents)
                .build();
    }

    public void assignParent(Reply parent) {
        this.parent = parent;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }

    public void addLike() {
        totalLike++;
    }

    public void subLike() {
        totalLike--;
    }
}