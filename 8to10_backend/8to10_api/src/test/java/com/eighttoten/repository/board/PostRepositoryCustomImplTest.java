package com.eighttoten.repository.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchPostPage;
import com.eighttoten.community.domain.post.PostWithReplies;
import com.eighttoten.community.domain.post.repository.PostRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostRepositoryCustomImplTest {
    @Autowired
    PostRepository postRepository;

    @Test
    @Transactional
    @DisplayName("게시글 페이지 조회")
    void searchPostDetailInfoInfoPreviewPages() {
        String keyword = "nick";
        String searchCond = "WRITER";
        String sortCond = "LIKE";
        String sortDirection = "ASC";
        SearchPostPage searchPostPage = new SearchPostPage(keyword, searchCond, sortCond, sortDirection, 1, 10);

        Pagination<PostPreview> pagination = postRepository.searchPostPreviewPages(searchPostPage);
        List<PostPreview> content = pagination.getContents();

        assertThat(content.size()).isEqualTo(10);
        assertThat(pagination.getTotalElements()).isEqualTo(45L);
        assertThat(pagination.getTotalPages()).isEqualTo(5);
    }

    @Test
    @DisplayName("게시글 단건 조회, 조회시 연관 댓글과 멤버도 함께 불러온다.")
    void search(){
        Long id = 1L;
        assertThat(post).isNotNull();
        assertThat(post.getReplies()).isNotNull();
    }
}