package com.eighttoten.community;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.domain.Member;
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
@Table(name = "post_scrap")
public class PostScrapEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_scrap_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity postEntity;

    public static PostScrapEntity from(PostEntity postEntity, MemberEntity memberEntity) {
        PostScrapEntity postScrapEntity = new PostScrapEntity();
        postScrapEntity.postEntity = postEntity;
        postScrapEntity.memberEntity = memberEntity;
        return postScrapEntity;
    }

    public PostScrap toPostScrap(){
        return new PostScrap(id, postEntity.toPost(), memberEntity.toMember(), createdBy);
    }
}