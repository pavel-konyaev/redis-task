package com.redis.repository;

import java.util.Set;

public interface FeedRepository {

    Set<String> getByStartAndEnd(long start, long end);

    long getCount();
}