package cool.yzt.cap;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cool.yzt.cap.entity.Comment;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import sun.nio.cs.US_ASCII;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class RedisTest {

    @Test
    public void test() {
        Jedis jedis = RedisUtil.getJedis();

        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        user.setPassword("12345");
        user.setActivationCode("1243");
        user.setSalt("123");
        user.setStatus(1);
        user.setType(1);
        user.setEmail("123@123");
        user.setHeaderUrl("http://www.baidu.com");
        user.setCreateTime(new Date());


        String json = JSONUtil.toJsonStr(user);
        User user1 = JSONUtil.toBean(json,User.class);
        System.out.println("原始对象:"+user);
        System.out.println("解析对象:"+user1);
        System.out.println("json字符串:"+json);
        System.out.println(user.getCreateTime().getTime());



    }

    @Test
    public void saveTest() {
        Jedis jedis = RedisUtil.getJedis();
        jedis.save();
        RedisUtil.close(jedis);
    }

    @Test
    public void test2() {

        Map<String,Object> map = new HashMap<>();
        map.put("username","张三");
        map.put("password","123");
        System.out.println("1:"+map);
        String jsonStr = JSONUtil.toJsonStr(map);
        System.out.println("2:"+jsonStr);
        Map<String,Object> jsonMap = JSONUtil.parseObj(jsonStr);
        System.out.println("3:"+jsonMap);
        System.out.println(map.get("username"));
        System.out.println(jsonMap.get("username"));
    }
}
