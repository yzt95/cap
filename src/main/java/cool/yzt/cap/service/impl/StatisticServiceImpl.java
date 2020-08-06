package cool.yzt.cap.service.impl;

import cool.yzt.cap.entity.StatisticData;
import cool.yzt.cap.mapper.StatisticMapper;
import cool.yzt.cap.service.StatisticService;
import cool.yzt.cap.util.DateUtil;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;

/**
 * @author : yzt
 */
@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public void recordUV(String cookie) {
        Jedis jedis = RedisUtil.getJedis();
        jedis.sadd(RedisKeyUtil.getUVKey(),cookie);
        RedisUtil.close(jedis);
    }

    @Override
    public void recordDAU(int userId) {
        Jedis jedis = RedisUtil.getJedis();
        jedis.sadd(RedisKeyUtil.getDAUKey(),String.valueOf(userId));
        RedisUtil.close(jedis);
    }

    @Override
    public void save(long uv, long dau, Date date) {
        date = DateUtil.getYMD(date);
        statisticMapper.insert(date,uv,dau);
    }

    @Override
    public StatisticData findByDate(Date date) {
        date = DateUtil.getYMD(date);
        return statisticMapper.selectByDate(date);
    }

    @Override
    public List<StatisticData> findForPeriod(Date from, Date to) {
        from = DateUtil.getYMD(from);
        to = DateUtil.getYMD(to);
        return statisticMapper.selectForPeriod(from,to);
    }
}
