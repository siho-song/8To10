package com.eighttoten.community.domain.post.repository;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.domain.post.NewPost;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchPostPage;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    void update(Post post);
    void save(NewPost newPost);
    void deleteById(Long id);
    Optional<Post> findById(Long id);
    Optional<PostDetailInfo> searchPostDetailInfo(Long memberId, Long postId);
    Pagination<PostPreview> searchPostPreviewPages(SearchPostPage searchPostPage);
    List<Post> findAllByMemberId(Long memberId);
}