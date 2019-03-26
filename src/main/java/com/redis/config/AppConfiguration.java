package com.redis.config;

import com.redis.controller.FeedController;
import com.redis.repository.FeedRepository;
import com.redis.repository.FeedRepositoryImpl;
import com.redis.service.FeedService;
import com.redis.service.FeedServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class AppConfiguration {

    @Autowired
    RedisTemplate redisTemplate;

    @Bean
    public FeedRepository feedRepository(){
        return new FeedRepositoryImpl(redisTemplate);
    }

    @Bean
    public FeedService feedService(){
        return new FeedServiceImpl(feedRepository());
    }

}
