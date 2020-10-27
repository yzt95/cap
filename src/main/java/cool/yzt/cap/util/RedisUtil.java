package cool.yzt.cap.util;

import redis.clients.jedis.*;
import java.io.IOException;
import java.util.Properties;

public class RedisUtil {
    private static final JedisPool pool;


    static {
        Properties pros = new Properties();
        try {
            pros.load(RedisUtil.class.getClassLoader().getResourceAsStream("redis.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(pros.getProperty("redis.pool.maxTotal")));
        config.setMaxIdle(Integer.parseInt(pros.getProperty("redis.pool.maxIdle")));
        String host = pros.getProperty("redis.host");
        int port = Integer.parseInt(pros.getProperty("redis.port"));
        String password = pros.getProperty("redis.password");
        int timeout = Integer.parseInt(pros.getProperty("redis.pool.maxWaitMillis"));
        int index = Integer.parseInt(pros.getProperty("redis.index"));
        pool = new JedisPool(config,host,port,timeout,password,index);
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void close(Jedis jedis) {
        jedis.close();
    }



}
