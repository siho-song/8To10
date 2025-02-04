package com.eighttoten.community.event.board;

import com.eighttoten.community.domain.post.Post;

public class PostHeartSubEvent extends PostStatsUpdateEvent {

    public PostHeartSubEvent(Long postId) {
        super(postId);
    }

    @Override
    public void execute(Post post) {
        post.subLike();
    }
}
