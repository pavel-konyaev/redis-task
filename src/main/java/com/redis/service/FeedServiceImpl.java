package com.redis.service;

import com.redis.model.FeedResponse;
import com.redis.model.Navigation;
import com.redis.repository.FeedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;

    @Override
    public FeedResponse getFeed(Long limit, Long next, Long prev){
        Set<String> items;
        long start;
        long end;
        long max = feedRepository.getCount() - 1;
        try {
            if (next != null && prev != null) {
                throw new IllegalArgumentException("both next and prev are set");
            } else if (next != null) {
                start = filterNegativeWithErrorAndMax(next, max);
                end = filterNegativeWithErrorAndMax(start + limit - 1, max);
                items = feedRepository.getByStartAndEnd(start, end);
            } else if (prev != null) {
                end = filterNegativeWithErrorAndMax(prev - 1, max);
                start = filterNegateAndMax(prev - limit, max);
                items = feedRepository.getByStartAndEnd(start, end);
            } else {
                start = 0l;
                end = filterNegativeWithErrorAndMax(limit - 1, max);
                items = feedRepository.getByStartAndEnd(start, end);
            }
        } catch (IllegalArgumentException e){
            log.info(String.format("limit = %d%n, next = %d%n, prev = %d%n", limit, next, prev), e); //info because it's not fatal error, it's wrong request
            return new FeedResponse();
        }
        return new FeedResponse(new ArrayList<>(items), getNavigation(start, end, max));
    }


    private Navigation getNavigation(Long start, Long end, Long max){
        String sPrev = String.valueOf(filterByMax(start, max)+1);
        boolean hasPrev = start.compareTo(0l) > 0;

        String sNext = String.valueOf(filterByMax(end, max)+1);
        boolean hasNext = end.compareTo(max) < 0;

        return Navigation.builder()
                .prev(sPrev)
                .hasPrev(hasPrev)
                .next(sNext)
                .hasNext(hasNext)
                .build();
    }

    private long filterNegativeWithErrorAndMax(Long l, Long max){
        return Optional.ofNullable(l).map(this::filterNegativeWithError).map(v -> filterByMax(v, max)).orElse(0l);
    }

    private long filterNegateAndMax(Long l, Long max){
        return Optional.ofNullable(l).map(this::filterNegative).map(v -> filterByMax(v, max)).orElse(0l);
    }

    private Long filterByMax(Long l, Long max){
        return (l.compareTo(max) >= 0) ? max : l;
    }

    private long filterNegativeWithError(Long l){
       if (l.compareTo(0l) >= 0){
           return l;
       } else {
           throw new IllegalArgumentException("Negative argument");
       }
    }

    private long filterNegative(Long l){
        return (l.compareTo(0l) >= 0) ? l : 0l;
    }

}