package cool.yzt.cap.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.FollowService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * 处理关注业务
 * @author : yzt
 */
@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private UserService userService;

    @Override
    public void follow(int followerId, int followedId) {
        String followerKey = RedisKeyUtil.getFollowerKey(followerId);
        String followedKey = RedisKeyUtil.getFollowedKey(followedId);
        Jedis jedis = RedisUtil.getJedis();
        Transaction multi = jedis.multi();
        long time = System.currentTimeMillis();

        try {
            // 在关注者的zset中添加被关注者
            multi.zadd(followerKey,time,String.valueOf(followedId));
            // 在被关注者zset中添加关注者
            multi.zadd(followedKey,time,String.valueOf(followerId));
            multi.exec();
        } catch (RuntimeException e) {
            multi.discard();
        }
        RedisUtil.close(jedis);
    }

    @Override
    public void unFollow(int followerId, int followedId) {
        String followerKey = RedisKeyUtil.getFollowerKey(followerId);
        String followedKey = RedisKeyUtil.getFollowedKey(followedId);
        Jedis jedis = RedisUtil.getJedis();
        // 如果关注了
        if(jedis.zrank(followerKey,String.valueOf(followedId))!=null) {
            Transaction multi = jedis.multi();
            try {
                multi.zrem(followerKey,String.valueOf(followedId));
                multi.zrem(followedKey,String.valueOf(followerId));
                multi.exec();
            } catch (RuntimeException e) {
                multi.discard();
            }
        }
        RedisUtil.close(jedis);
    }

    @Override
    public boolean isFollow(int followerId, int followedId) {
        String followerKey = RedisKeyUtil.getFollowerKey(followerId);
        Jedis jedis = RedisUtil.getJedis();
        boolean flag = jedis.zrank(followerKey,String.valueOf(followedId))!=null;
        RedisUtil.close(jedis);
        return flag;
    }

    @Override
    public PageBean findFollower(int targetUserId, int selfId, int start,int limit) {
        String followedKey = RedisKeyUtil.getFollowedKey(targetUserId);
        Jedis jedis = RedisUtil.getJedis();
        long totalCount = jedis.zcard(followedKey);
        long endIndex = totalCount-1-(start-1)*limit;
        long startIndex = Math.max(endIndex-limit+1,0);

        Set<Tuple> followers = jedis.zrevrangeWithScores(followedKey, startIndex, endIndex);
        RedisUtil.close(jedis);
        PageBean pageBean = new PageBean();
        List<Map<String,Object>> contents = new ArrayList<>();
        for(Tuple tuple : followers) {
            Map<String,Object> content = new HashMap<>();
            Date date = new Date((long)tuple.getScore());
            content.put("date",date);
            User user = userService.findById(Integer.parseInt(tuple.getElement()));
            content.put("user",user);
            content.put("isFollow",isFollow(selfId,user.getId()));
            content.put("isSelf",user.getId()==selfId);
            contents.add(content);
        }
        pageBean.setContents(contents);
        pageBean.setCurrent(start);
        pageBean.setSize(limit);
        pageBean.setTotalContent(totalCount);
        pageBean.setTotalPage(((int)totalCount/limit)+1);
        pageBean.setPrePage(start-1);
        pageBean.setNextPage(start+1);
        pageBean.setFirst(start==1);
        pageBean.setLast(start==pageBean.getTotalPage());
        return pageBean;
    }

    @Override
    public PageBean findFollowing(int targetUserId, int selfId, int start,int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(targetUserId);
        Jedis jedis = RedisUtil.getJedis();
        long totalCount = jedis.zcard(followerKey);
        long endIndex = totalCount-1-(start-1)*limit;
        long startIndex = Math.max(endIndex-limit+1,0);

        Set<Tuple> followers = jedis.zrevrangeWithScores(followerKey, startIndex, endIndex);
        RedisUtil.close(jedis);
        PageBean pageBean = new PageBean();
        List<Map<String,Object>> contents = new ArrayList<>();
        for(Tuple tuple : followers) {
            Map<String,Object> content = new HashMap<>();
            Date date = new Date((long)tuple.getScore());
            content.put("date",date);
            User user = userService.findById(Integer.parseInt(tuple.getElement()));
            content.put("user",user);
            content.put("isFollow",isFollow(selfId,user.getId()));
            content.put("isSelf",user.getId()==selfId);
            contents.add(content);
        }
        pageBean.setContents(contents);
        pageBean.setCurrent(start);
        pageBean.setSize(limit);
        pageBean.setTotalContent(totalCount);
        pageBean.setTotalPage(((int)totalCount/limit)+1);
        pageBean.setPrePage(start-1);
        pageBean.setNextPage(start+1);
        pageBean.setFirst(start==1);
        pageBean.setLast(start==pageBean.getTotalPage());
        return pageBean;
    }

    @Override
    public long findFollowerCount(int userId) {
        String followedKey = RedisKeyUtil.getFollowedKey(userId);
        Jedis jedis = RedisUtil.getJedis();
        long totalCount = jedis.zcard(followedKey);
        RedisUtil.close(jedis);
        return totalCount;
    }

    @Override
    public long findFollowedCount(int userId) {
        String followerKey = RedisKeyUtil.getFollowerKey(userId);
        Jedis jedis = RedisUtil.getJedis();
        long totalCount = jedis.zcard(followerKey);
        RedisUtil.close(jedis);
        return totalCount;
    }
}
