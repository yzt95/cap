package cool.yzt.cap;

import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.UserMapper;
import cool.yzt.cap.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class RegisterTest {
    @Autowired
    private UserService userService;

    @Test
    public void registerTest() {
        User user = new User();
        user.setUsername("yangzitao");
        user.setPassword("123456");
        user.setEmail("yzt@qq.com");

        int msg = userService.register(user);
        System.out.println(msg);
    }
}
