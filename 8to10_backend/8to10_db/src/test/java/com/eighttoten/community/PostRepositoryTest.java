package com.eighttoten.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.domain.post.NewPost;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchPostPage;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.repository.post.PostRepositoryImpl;
import com.eighttoten.member.repository.MemberRepositoryImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

@DataJpaTest
@DisplayName("게시글 레포지토리 테스트")
@Import({PostRepositoryImpl.class, MemberRepositoryImpl.class})
class PostRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글을 id로 조회한다.")
    void findById() {
        //given
        Long postId = 1L;

        //when
        Post post = postRepository.findById(postId).orElseThrow();

        //then
        assertThat(post).isNotNull();
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    void save() {
        //given
        Long memberId = 1L;
        NewPost newPost = new NewPost("새로운 게시글", "내용", memberId);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("testUser"));

        //when,then
        assertThatCode(() -> postRepository.save(newPost)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게시글의 내용,제목, 총 좋아요 수 , 총 스크랩 수를 업데이트한다.")
    void update() {
        //given
        Long postId = 1L;
        Post updatedPost = new Post(postId, "새로운 제목", "새로운 내용", null, null, 100, 100);

        //when
        postRepository.update(updatedPost);

        //then
        Post post = postRepository.findById(postId).orElseThrow();
        assertThat(post.getTitle()).isEqualTo(updatedPost.getTitle());
        assertThat(post.getContents()).isEqualTo(updatedPost.getContents());
        assertThat(post.getTotalLike()).isEqualTo(updatedPost.getTotalLike());
        assertThat(post.getTotalScrap()).isEqualTo(updatedPost.getTotalScrap());
    }

    @Test
    @DisplayName("게시글 프리뷰 페이지를 조회한다.")
    void searchPostDetailInfoInfoPreviewPages() {
        //given
        String keyword = "nick";
        String searchCond = "WRITER";
        String sortCond = "LIKE";
        String sortDirection = "ASC";
        SearchPostPage searchPostPage = new SearchPostPage(keyword, searchCond, sortCond, sortDirection, 1, 10);

        //when
        Pagination<PostPreview> pagination = postRepository.searchPostPreviewPages(searchPostPage);
        List<PostPreview> content = pagination.getContents();

        //then
        assertThat(content.size()).isEqualTo(10);
        assertThat(pagination.getTotalElements()).isEqualTo(45L);
        assertThat(pagination.getTotalPages()).isEqualTo(4);
    }

    @Test
    @DisplayName("게시글 세부사항을 조회한다.")
    void searchPostDetailInfo() {
        //given
        Long memberId = 1L;
        Long postId = 1L;

        //when
        PostDetailInfo postDetailInfo = postRepository.searchPostDetailInfo(memberId, postId).orElseThrow();

        //then
        assertThat(postDetailInfo.getId()).isNotNull();
    }

    @Test
    @DisplayName("멤버의 게시물을 모두 조회한다.")
    void findAllByMemberId(){
        //given
        Long memberId = 1L;

        //when
        List<Post> posts = postRepository.findAllByMemberId(memberId);

        //then
        assertThat(posts.size()).isGreaterThan(1);
    }
}