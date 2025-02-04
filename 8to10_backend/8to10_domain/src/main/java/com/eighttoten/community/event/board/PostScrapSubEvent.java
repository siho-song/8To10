package com.eighttoten.community.event.board;

import com.eighttoten.community.domain.post.Post;

public class PostScrapSubEvent extends PostStatsUpdateEvent {

    public PostScrapSubEvent(Long postId) {
        super(postId);
    }

    @Override
    public void execute(Post post) {
        post.subScrap();
    }

}
