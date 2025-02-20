package com.eighttoten.support;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagePublisherImpl implements MessagePublisher {
    private final RedisOperations<String,String> redisOperations;
    private final ChannelTopic notificationTopic;

    @Override
    public void send(String message) {
        redisOperations.convertAndSend(notificationTopic.getTopic(), message);
    }
}
