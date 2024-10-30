package com.sweet;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;

@SpringBootTest
public class ReidsTest {
//    @Resource
//    private RedisTemplate<String, String> redisTemplateString;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

//    @Test
//    void set() {
//        redisTemplateString.opsForValue().set("name", "sweet");
//    }
//
//    @Test
//    void get() {
//        String name = redisTemplateString.opsForValue().get("name");
//        System.out.println(name);
//    }
//
//    @Test
//    void delete() {
//        redisTemplateString.delete("name");
//    }

    @Test
    void analyzeKeys() {
        Set<String> keys = redisTemplate.keys("*");
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        if (keys == null || keys.size() == 0) {
            return;
        }

        for (String key : keys) {
            if (key.contains("like") && key.contains("count") && !keys.contains("dislike")) { // 点赞数
                String poemId = key.split(":")[1];
                Integer count = ops.get(key);
                if (count == null) {
                    count = 0;
                }
                System.out.println(poemId + "===like===" + count);
            } else if (key.contains("dislike") && key.contains("count")) { // 踩数
                String poemId = key.split(":")[1];
                Integer count = ops.get(key);
                if (count == null) {
                    count = 0;
                }
                System.out.println(poemId + "===dislike===" + count);
            } else if (key.contains("favorite") && key.contains("count")) { // 收藏数
                String poemId = key.split(":")[1];
                Integer count = ops.get(key);
                if (count == null) {
                    count = 0;
                }
                System.out.println(poemId + "===favorite===" + count);
            } else if (key.contains("like") && !key.contains("count")) { // 用户点赞古诗
                String userId = key.split(":")[1];
                String poemId = key.split(":")[2];
                Integer isLike = ops.get(key);
                if (isLike != null && isLike == 1) {
                    System.out.println(userId + "===like IS===" + poemId);
                }
            } else if (key.contains("dislike") && !key.contains("count")) { // 用户踩古诗
                String userId = key.split(":")[1];
                String poemId = key.split(":")[2];
                Integer isDisLike = ops.get(key);
                if (isDisLike != null && isDisLike == 1) {
                    System.out.println(userId + "===dislike IS===" + poemId);
                }
            } else if (key.contains("favorite") && !key.contains("count")) { // 用户收藏古诗
                String userId = key.split(":")[1];
                String poemId = key.split(":")[2];
                Integer isFavorite = ops.get(key);
                if (isFavorite != null && isFavorite == 1) {
                    System.out.println(userId + "===favorite IS===" + poemId);
                }
            }
        }
    }




}
