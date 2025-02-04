package com.eighttoten.community.service;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_POST;

import com.eighttoten.common.Pagination;
import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.NewPost;
import com.eighttoten.community.domain.post.UpdatePost;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostPreview;
import com.eighttoten.community.domain.post.SearchPostPage;
import com.eighttoten.community.domain.post.repository.PostHeartRepository;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostHeartRepository postHeartRepository;
    private final PostScrapRepository postScrapRepository;
    private final ReplyHeartRepository replyHeartRepository;
    private final ReplyRepository replyRepository;

    @Transactional(readOnly = true)
    public Pagination<PostPreview> searchPostPreviewPages(SearchPostPage searchPostPage) {
        return postRepository.searchPostPreviewPages(searchPostPage);
    }

    @Transactional(readOnly = true)
    public PostDetailInfo searchPostDetailInfo(Long memberId, Long postId) {
        return postRepository.searchPostDetailInfo(memberId, postId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));
    }

    @Transactional
    public void save(NewPost newPost) {
        postRepository.save(newPost);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));

        List<Reply> replies = replyRepository.findAllByPostId(post.getId());

        if (member.isSameEmail(post.getCreatedBy())) {
            postHeartRepository.deleteHeartsByPostId(id);
            postScrapRepository.deleteScrapByPostId(id);
            replyHeartRepository.deleteByReplyIds(replies
                    .stream()
                    .map(Reply::getId)
                    .toList()
            );
            postRepository.deleteById(id);
        }
    }

    @Transactional
    public void update(Member member, UpdatePost updatePost) {
        Post post = postRepository.findById(updatePost.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));

        if (member.isSameEmail(post.getCreatedBy())) {
            post.update(updatePost);
            postRepository.update(post);
        }
    }
}