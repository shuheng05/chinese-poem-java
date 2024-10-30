package com.sweet.common.task;

import com.sweet.poem.mapper.PoemMapper;
import com.sweet.poem.protocol.po.UserDislikePoemPO;
import com.sweet.poem.protocol.po.UserLikePoemPO;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 定时任务任务工具类
 *
 * @author shuheng
 */
@Component
public class TimedTask {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private PoemMapper poemMapper;

    /**
     * 定时任务，将redis中点赞、踩、收藏数据同步到mysql中
     * 每6小时执行一次
     */
    //@Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 0/6 * * ?")
    public void redisPoemLikeDislikeFavoriteToMysql() {
        Set<String> keys = redisTemplate.keys("*");
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();

        if (keys == null || keys.size() == 0) {
            return;
        }

        for (String key : keys) {
            if (key.contains("like") && key.contains("count") && !key.contains("dislike")) { // 点赞数
                String poemId = key.split(":")[1];
                Integer count = ops.get(key);
                if (count == null) {
                    count = 0;
                }
                poemMapper.updateLikeCountById(Integer.valueOf(poemId), count);
            } else if (key.contains("dislike") && key.contains("count")) { // 踩数
                String poemId = key.split(":")[1];
                Integer count = ops.get(key);
                if (count == null) {
                    count = 0;
                }
                poemMapper.updateDislikeCountById(Integer.valueOf(poemId), count);
            } else if (key.contains("favorite") && key.contains("count")) { // 收藏数
                String poemId = key.split(":")[1];
                Integer count = ops.get(key);
                if (count == null) {
                    count = 0;
                }
                poemMapper.updateFavoriteCountById(Integer.valueOf(poemId), count);
            } else if (key.contains("like") && !key.contains("count") && !key.contains("dislike")) { // 用户点赞古诗
                String userId = key.split(":")[1];
                String poemId = key.split(":")[2];
                Integer isLike = ops.get(key);
                if (isLike != null && isLike == 1) {
                    UserLikePoemPO userLikePoempo = poemMapper.selectFromUserLikePoem(userId, Integer.valueOf(poemId));
                    if (userLikePoempo == null) {
                        poemMapper.insertToUserLikePoem(userId, Integer.valueOf(poemId));
                    }
                } else if (isLike != null && isLike == 0) {
                    poemMapper.deleteFromUserlikePoem(userId, Integer.valueOf(poemId));
                }
            } else if (key.contains("dislike") && !key.contains("count")) { // 用户踩古诗
                String userId = key.split(":")[1];
                String poemId = key.split(":")[2];
                Integer isDislike = ops.get(key);
                if (isDislike != null && isDislike == 1) {
                    UserDislikePoemPO userDislikePoempo = poemMapper.selectFromUserDisikePoem(userId, Integer.valueOf(poemId));
                    if (userDislikePoempo == null) {
                        poemMapper.insertToUserDislikePoem(userId, Integer.valueOf(poemId));
                    }
                } else if (isDislike != null && isDislike == 0) {
                    poemMapper.deleteFromUserDislikePoem(userId, Integer.valueOf(poemId));
                }
            } // RabbitMQ
//            else if (key.contains("favorite") && !key.contains("count")) { // 用户收藏古诗
//                String userId = key.split(":")[1];
//                String poemId = key.split(":")[2];
//                Integer isLike = ops.get(key);
//                if (isLike != null && isLike == 1) {
//                    poemMapper.insertToUserFavoritesPoem(userId, Integer.valueOf(poemId));
//                } else if (isLike != null && isLike == 0) {
//                    poemMapper.deleteFromUserFavoritesPoem(userId, Integer.valueOf(poemId));
//                }
//            }
        }

    }
}
