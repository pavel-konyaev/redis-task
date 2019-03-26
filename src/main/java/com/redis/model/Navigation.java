package com.redis.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class Navigation {

    private String prev;
    private String next;

    private boolean hasPrev;
    private boolean hasNext;
}
