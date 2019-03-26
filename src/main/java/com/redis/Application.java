package com.redis;

import com.redis.repository.FeedRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class Application implements WebMvcConfigurer {

    final RedisServer redisServer;
    final StringRedisTemplate template;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void start() {

        // embedded redis doesn't work on my OS. but this is initialization

        /*if (!redisServer.isActive()) redisServer.start();
        log.info("redis listen ports: {}", redisServer.ports().stream()
                .map(Object::toString).collect(joining(",")));*/

        setUpData();
    }

    @PreDestroy
    public void stop() {
        /*log.info("shutting down redis...");
        redisServer.stop();
        log.info("bye!");*/
    }

    private void setUpData() {
        log.info("------settings up data for redis------");

        for (int i=0; i<10000; i++) {
            template.opsForZSet().add(FeedRepositoryImpl.FEED_KEY, "el"+(i+1), i);
        }

        log.info("------data has been set------");
    }

}
