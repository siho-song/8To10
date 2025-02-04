package com.eighttoten.community.repository.post;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchPostPage;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<PostDetailInfo> searchPostDetailInfo(Long memberId, Long postId);
    Pagination<PostPreview> searchPostPreviewPages(SearchPostPage cond);
}