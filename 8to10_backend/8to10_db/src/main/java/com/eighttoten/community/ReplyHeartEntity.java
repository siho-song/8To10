package com.eighttoten.community;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.community.domain.reply.NewReplyHeart;
import com.eighttoten.community.domain.reply.ReplyHeart;
import com.eighttoten.member.MemberEntity;
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
public class ReplyHeartEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private ReplyEntity replyEntity;

    public static ReplyHeartEntity from(MemberEntity memberEntity, ReplyEntity replyEntity) {
        ReplyHeartEntity replyHeartEntity = new ReplyHeartEntity();
        replyHeartEntity.replyEntity = replyEntity;
        replyHeartEntity.memberEntity = memberEntity;
        return replyHeartEntity;
    }

    public ReplyHeart toReplyHeart(){
        return new ReplyHeart(id, replyEntity.toReply(), memberEntity.toMember(), createdBy);
    }
}