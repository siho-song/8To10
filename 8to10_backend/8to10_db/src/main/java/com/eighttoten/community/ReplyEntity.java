package com.eighttoten.community;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.ReplyDetailInfo;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import com.eighttoten.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "reply")
public class ReplyEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity postEntity;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    private long totalLike;

    public static ReplyEntity from(NewReply newReply, MemberEntity memberEntity, PostEntity postEntity) {
        ReplyEntity replyEntity = new ReplyEntity();
        replyEntity.memberEntity = memberEntity;
        replyEntity.postEntity = postEntity;
        replyEntity.parentId = newReply.getParentId();
        replyEntity.contents = newReply.getContents();
        return replyEntity;
    }

    public Reply toReply(){
        return new Reply(id, parentId, postEntity.getId(), contents, createdBy, createdAt, totalLike);
    }

    public ReplyWithPost toReplyWithPost(){
        return new ReplyWithPost(id, parentId, postEntity.toPost(), contents, createdBy, createdAt, totalLike);
    }

    public ReplyDetailInfo toReplyDetailInfo(){
        return new ReplyDetailInfo(id, parentId, contents,
                memberEntity.getNickname(), createdBy,
                createdAt, updatedAt, totalLike);
    }

    public void update(Reply reply){
        this.contents = reply.getContents();
        this.totalLike = reply.getTotalLike();
    }
}