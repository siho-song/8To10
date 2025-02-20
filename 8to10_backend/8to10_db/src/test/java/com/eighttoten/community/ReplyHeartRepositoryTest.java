package com.eighttoten.community;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import com.eighttoten.community.domain.reply.NewReplyHeart;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.community.repository.ReplyHeartRepositoryImpl;
import java.util.ArrayList;
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
@DisplayName("댓글 좋아요 레포지토리 테스트")
@Import(ReplyHeartRepositoryImpl.class)
public class ReplyHeartRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    ReplyHeartRepository replyHeartRepository;

    @Test
    @DisplayName("게시글 좋아요를 저장한다.")
    void save(){
        //given
        NewReplyHeart newReplyHeart = NewReplyHeart.from(1L, 3L);
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when,then
        assertThatCode(() -> replyHeartRepository.save(newReplyHeart)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("멤버의 id, 게시글의 id로 댓글 좋아요가 있는지 확인한다.")
    void existsByMemberIdAndPostId(){
        //given
        Long memberId = 1L;
        Long replyId = 1L;

        //when
        boolean isTrue = replyHeartRepository.existsByMemberIdAndReplyId(memberId, replyId);

        //then
        assertThat(isTrue).isTrue();
    }

    @Test
    @DisplayName("댓글의 id로 댓글에 저장된 모든 좋아요를 삭제한다.")
    void deleteAllByReplyId(){
        //given
        Long memberId = 1L;
        Long replyId = 10L;
        boolean beforeStatus = replyHeartRepository.existsByMemberIdAndReplyId(memberId, replyId);

        //when,then
        assertThatCode(() -> replyHeartRepository.deleteAllByReplyId(replyId)).doesNotThrowAnyException();
        assertThat(replyHeartRepository.existsByMemberIdAndReplyId(memberId,replyId)).isNotEqualTo(beforeStatus);
    }

    @Test
    @DisplayName("댓글의 id 리스트로 댓글에 저장된 모든 좋아요들을 삭제한다.")
    void deleteAllByReplyIds(){
        //given
        Long memberId1 = 2L;
        Long memberId2 = 3L;
        Long replyId1 = 2L;
        Long replyId2 = 3L;
        List<Long> replyIds = new ArrayList<>();
        replyIds.add(replyId1);
        replyIds.add(replyId2);
        boolean beforeStatus1 = replyHeartRepository.existsByMemberIdAndReplyId(memberId1, replyId1);
        boolean beforeStatus2 = replyHeartRepository.existsByMemberIdAndReplyId(memberId2, replyId2);

        //when,then
        assertThatCode(() -> replyHeartRepository.deleteAllByReplyIds(replyIds)).doesNotThrowAnyException();
        assertThat(replyHeartRepository.existsByMemberIdAndReplyId(memberId1,replyId1)).isNotEqualTo(beforeStatus1);
        assertThat(replyHeartRepository.existsByMemberIdAndReplyId(memberId2,replyId2)).isNotEqualTo(beforeStatus2);
    }

    @Test
    @DisplayName("멤버의 id, 게시글의 id로 게시글 좋아요를 삭제한다.")
    void deleteByMemberIdAndReplyId(){
        //given
        Long memberId = 1L;
        Long replyId = 4L;

        //when
        replyHeartRepository.deleteByMemberIdAndReplyId(memberId, replyId);

        //then
        assertThat(replyHeartRepository.existsByMemberIdAndReplyId(1L,4L)).isFalse();
    }
}
