package com.eighttoten.community;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.NewPost;
import com.eighttoten.member.MemberEntity;
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
public class PostEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    private long totalLike;

    private long totalScrap;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyEntity> replyEntities;

    public static PostEntity from(NewPost newPost, MemberEntity memberEntity) {
        PostEntity postEntity = new PostEntity();
        postEntity.title = newPost.getTitle();
        postEntity.contents = newPost.getContents();
        postEntity.memberEntity = memberEntity;
        return postEntity;
    }

    public Post toPost() {
        return new Post(id, title, contents, createdBy, createdAt, totalLike, totalScrap);
    }

    public void update(Post post){
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.totalLike = post.getTotalLike();
        this.totalScrap = post.getTotalScrap();
    }
}