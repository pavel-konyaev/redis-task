package com.redis.config.properties;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app.properties")
@Data
public class RedisProperties {

    private String hostname;
    private int port;
}
