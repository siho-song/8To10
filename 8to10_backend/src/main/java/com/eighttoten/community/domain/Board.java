package com.eighttoten.community.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.community.dto.BoardSaveRequest;
import com.eighttoten.global.auditing.baseentity.BaseTimeEntity;
import com.eighttoten.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Board extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    private long totalLike;

    private long totalScrap;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> replies;

    public static Board from(Member member , BoardSaveRequest dto){
        Board board = new Board();
        board.member = member;
        board.title = dto.getTitle();
        board.contents = dto.getContents();
        return board;
    }

    public void update(String title, String content){
        this.title = title;
        this.contents = content;
    }

    public void addLike(){
        totalLike++;
    }
    public void subLike() { totalLike--; }
    public void addScrap() { totalScrap++; }
    public void subScrap() { totalScrap--; }
}