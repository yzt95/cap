package cool.yzt.cap.service.impl;

import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@Service
public class LikeServiceImpl implements LikeService {
    @Override
    public void like(int userId, int entityType, int entityId, int authorId) {
        Jedis jedis = RedisUtil.getJedis();
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        String entityLikeValue = Integer.toString(userId);
        String userLikeKey = RedisKeyUtil.getUserLikeKey(authorId);
        if(jedis.sismember(entityLikeKey,entityLikeValue)){
            Transaction multi = jedis.multi();
            try {
                multi.srem(entityLikeKey,entityLikeValue);
                multi.decr(userLikeKey);
                multi.exec();
            } catch (RuntimeException e) {
                multi.discard();
            }
        } else {
            Transaction multi = jedis.multi();
            try {
                multi.sadd(entityLikeKey,entityLikeValue);
                multi.incr(userLikeKey);
                multi.exec();
            } catch (RuntimeException e) {
                multi.discard();
            }
        }
        RedisUtil.close(jedis);
    }

    @Override
    public long findEntityLikeCount(int entityType, int entityId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Jedis jedis = RedisUtil.getJedis();
        long count = jedis.scard(key);
        RedisUtil.close(jedis);
        return count;
    }

    @Override
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        String value = Integer.toString(userId);
        Jedis jedis = RedisUtil.getJedis();
        int ret = jedis.sismember(key,value) ? 1 : 0;
        RedisUtil.close(jedis);
        return ret;
    }

    @Override
    public long findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Jedis jedis = RedisUtil.getJedis();
        String count = jedis.get(userLikeKey);
        RedisUtil.close(jedis);
        return StringUtils.isBlank(count) ? 0 : Long.parseLong(count);
    }
}
