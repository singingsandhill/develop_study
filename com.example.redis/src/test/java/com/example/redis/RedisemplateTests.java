package com.example.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisemplateTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void stringOpsTest(){
        ValueOperations<String,String> ops
                // 지금 redisTemplate에 설정된 타입을 바탕으로 Redis에 문자열 조작
                 = stringRedisTemplate.opsForValue();
        ops.set("simpleKey","simpleValue");
        System.out.println("simplekey");

//        집합을 조작하기 위한 클래스
        SetOperations<String,String> setOps
                = stringRedisTemplate.opsForSet();
        setOps.add("hobbies","games");
        setOps.add("hobbies","coding","alcohol","games");
        System.out.println(setOps.size("hobbies"));

        stringRedisTemplate.expire("hobbies",10, TimeUnit.SECONDS);
        stringRedisTemplate.delete("simpleKey");
    }
}
