package show.schedulemanagement.domain.board.reply;

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
import show.schedulemanagement.domain.auditing.baseEntity.BaseTimeEntity;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;

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
    private String content;

    private long totalHearts;

    public static Reply from(Reply parent, String contents, Member member, Board board) {
        return Reply.builder()
                .member(member)
                .board(board)
                .parent(parent)
                .content(contents)
                .build();
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void addLike() {
        totalHearts++;
    }
}