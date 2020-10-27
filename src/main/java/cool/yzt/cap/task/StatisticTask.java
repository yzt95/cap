package cool.yzt.cap.task;

import cool.yzt.cap.service.StatisticService;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * @author : yzt
 */
@Component
public class StatisticTask {

    @Autowired
    private StatisticService statisticService;

    // 每天能晚上23：59执行
    @Scheduled(cron = "0 59 23 ? * *")
    public void refreshStatisticData() {
        Date date = new Date();
        String uvKey = RedisKeyUtil.getUVKey();
        String dauKey = RedisKeyUtil.getDAUKey();
        Jedis jedis = RedisUtil.getJedis();
        //long uv = jedis.scard(uvKey);
        long uv = jedis.pfcount(uvKey);
        jedis.del(uvKey);
        //long dau = jedis.scard(dauKey);
        long dau = jedis.pfcount(dauKey);
        jedis.del(dauKey);
        RedisUtil.close(jedis);
        statisticService.save(uv,dau,date);
    }

}
