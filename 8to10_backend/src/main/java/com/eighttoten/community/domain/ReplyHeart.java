package com.eighttoten.community.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.member.domain.Member;
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
public class ReplyHeart {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static ReplyHeart of(Reply reply, Member member) {
        ReplyHeart replyHeart = new ReplyHeart();
        replyHeart.reply = reply;
        replyHeart.member = member;
        return replyHeart;
    }
}