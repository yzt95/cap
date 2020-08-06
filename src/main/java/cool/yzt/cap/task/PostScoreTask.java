package cool.yzt.cap.task;

import cool.yzt.cap.service.DiscussPostService;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author : yzt
 */
@Component
public class PostScoreTask {
    @Autowired
    private DiscussPostService discussPostService;

    @Async
    public void recordPostId(int postId) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getChangedPostKey();
        jedis.sadd(key,String.valueOf(postId));
        RedisUtil.close(jedis);
    }

    // 每隔6小时执行一次
    @Scheduled(cron = "0 0 0/6 * * ?")
    //@Scheduled(cron = "0 0/3 * * * ?")
    public void calculateScore() {
        Jedis jedis = RedisUtil.getJedis();
        Set<String> posts = jedis.smembers(RedisKeyUtil.getChangedPostKey());
        if(posts==null||posts.isEmpty()) {
            RedisUtil.close(jedis);
            return;
        }
        jedis.del(RedisKeyUtil.getChangedPostKey());
        RedisUtil.close(jedis);
        for(String s : posts) {
            int postId = Integer.parseInt(s);
            discussPostService.calculateScore(postId);
        }
    }
}
