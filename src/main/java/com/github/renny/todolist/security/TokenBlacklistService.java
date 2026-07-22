package com.github.renny.todolist.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    private final RedisTemplate<String,String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist";

    public TokenBlacklistService(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token,long expirationTimeMillis){
        if(expirationTimeMillis <= 0 ){
            return;
        }

        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key,"true",expirationTimeMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklist(String token){
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}
