package com.eighttoten.community.repository.post;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.PostEntity;
import com.eighttoten.community.domain.post.NewPost;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchPostPage;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final PostJpaRepository postRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    public void update(Post post) {
        PostEntity entity = postRepository.findById(post.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_POST));
        entity.update(post);
    }

    @Override
    public void save(NewPost newPost) {
        MemberEntity memberEntity = memberRepository.findById(newPost.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        postRepository.save(PostEntity.from(newPost, memberEntity));
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id).map(PostEntity::toPost);
    }

    @Override
    public Optional<PostDetailInfo> searchPostDetailInfo(Long memberId, Long postId) {
        return postRepository.searchPostDetailInfo(memberId, postId);
    }

    @Override
    public Pagination<PostPreview> searchPostPreviewPages(SearchPostPage searchPostPage) {
        return postRepository.searchPostPreviewPages(searchPostPage);
    }

    @Override
    public List<Post> findAllByMemberId(Long memberId) {
        List<PostEntity> entities = postRepository.findAllByMemberEntityId(memberId);
        return entities.stream().map(PostEntity::toPost).toList();
    }
}