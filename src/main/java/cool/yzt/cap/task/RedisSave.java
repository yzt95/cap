package cool.yzt.cap.task;

import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author : yzt
 */
@Component
public class RedisSave {
    // 每隔6小时执行一次
    @Scheduled(cron = "0 0 0/6 * * ?")
    public void save() {
        Jedis jedis = RedisUtil.getJedis();
        jedis.save();
        RedisUtil.close(jedis);
    }
}
