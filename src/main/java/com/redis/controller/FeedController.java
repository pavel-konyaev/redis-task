package com.redis.controller;

import com.redis.model.FeedResponse;
import com.redis.service.FeedService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @RequestMapping(value = "feed", method = RequestMethod.GET)
    @ResponseBody
    public FeedResponse getFeed(@RequestParam Long limit,
                                @RequestParam(name = "next", required = false) Long next,
                                @RequestParam(name = "prev", required = false) Long prev) {
        return feedService.getFeed(limit, next, prev);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String hello() {
        return "Hello world";
    }
}