package com.redis.service;

import com.redis.model.FeedResponse;

public interface FeedService {

    FeedResponse getFeed(Long limit, Long next, Long prev);
}