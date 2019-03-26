package com.redis.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@AllArgsConstructor
public class FeedRepositoryImpl implements FeedRepository {

    private final RedisTemplate redisTemplate;

    public static final String FEED_KEY = "FEED";

    @Override
    public Set<String> getByStartAndEnd(long start, long end){
        return redisTemplate.opsForZSet().range(FEED_KEY, start, end);
    }

    @Override
    public long getCount(){
        return redisTemplate.opsForZSet().zCard(FEED_KEY);
    }

}