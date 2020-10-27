package cool.yzt.cap.service.impl;

import cn.hutool.json.JSONUtil;
import cool.yzt.cap.entity.LoginTicket;
import cool.yzt.cap.service.LoginTicketService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class LoginTicketServiceImpl implements LoginTicketService {
    @Autowired
    private UserService userService;

    @Override
    public int save(LoginTicket loginTicket,int expiredSeconds) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getLoginTicketKey(loginTicket.getTicket());
        String value = JSONUtil.toJsonStr(loginTicket);
        jedis.set(key,value);
        jedis.expire(key, expiredSeconds);
        RedisUtil.close(jedis);
        return 1;
    }

    @Override
    public LoginTicket findByTicket(String ticket) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getLoginTicketKey(ticket);
        String value = jedis.get(key);
        RedisUtil.close(jedis);
        if(value==null) return null;
        return JSONUtil.toBean(value,LoginTicket.class);
    }

    @Override
    public void logout(String ticket) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getLoginTicketKey(ticket);
        LoginTicket loginTicket = JSONUtil.toBean(jedis.get(key),LoginTicket.class);
        loginTicket.setStatus(0);
        String value = JSONUtil.toJsonStr(loginTicket);
        jedis.set(key,value);
        RedisUtil.close(jedis);
        userService.deleteInCache(loginTicket.getUserId());
    }
}
