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
import show.schedulemanagement.domain.member.Member;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class ReplyHearts{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_hearts_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}