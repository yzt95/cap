package cool.yzt.cap;

import cool.yzt.cap.entity.Comment;
import cool.yzt.cap.entity.DiscussPost;
import cool.yzt.cap.entity.LoginTicket;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.CommentMapper;
import cool.yzt.cap.mapper.DiscussPostMapper;
import cool.yzt.cap.mapper.LoginTicketMapper;
import cool.yzt.cap.mapper.UserMapper;
import cool.yzt.cap.util.GeneralUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void testUserMapperSelect() {
        User user1 = userMapper.selectById(101);
        System.out.println(user1);

        User user2 = userMapper.selectByUsername("liubei");
        System.out.println(user2);

        User user3 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);

        System.out.println(user1==user2);
    }

    @Test
    public void testUserMapperInsert() {
        User saveUser = new User();
        saveUser.setUsername("yzt");
        saveUser.setEmail("yzt@163.com");
        saveUser.setPassword("12345");
        saveUser.setSalt("123");
        saveUser.setHeaderUrl("https://www.baiduc.com");
        saveUser.setActivationCode("1212");
        saveUser.setStatus(1);
        saveUser.setType(1);
        saveUser.setCreateTime(new Date());

        System.out.println(userMapper.insert(saveUser));
    }

    @Test
    public void testUserMapperUpdate() {
        System.out.println(userMapper.updateStatus(150,0));
        System.out.println(userMapper.updatePassword(150,"yzt123"));
        System.out.println(userMapper.updateHeaderUrl(150,"cool.yzt"));
    }



    @Test
    public void testLoginTicketMapperInsert() {
        LoginTicket loginTicket = new LoginTicket();
        String ticket = GeneralUtil.getUUId();
        loginTicket.setTicket(ticket);
        loginTicket.setUserId(1003);
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*60*24*3));
        int insertResult = loginTicketMapper.insert(loginTicket);
        System.out.println(insertResult);
    }

    @Test
    public void testLoginTicketMapperSelect() {
        LoginTicket loginTicket = loginTicketMapper.select("e5f911f87c6546138825f0843dfe379d");
        System.out.println(loginTicket);
    }

    @Test
    public void testLoginTicketMapperUpdateStatus() {
        int ret = loginTicketMapper.updateStatus("e5f911f87c6546138825f0843dfe379d",1);
        System.out.println(ret);
    }

    @Test
    public void testCommentMapper() {
        int entityType = 1;
        int entityId = 228;
        /*List<Comment> comments = commentMapper.selectByEntity(entityType,entityId);
        for(Comment comment : comments) {
            System.out.println(comment.getId()+"  "+comment.getContent());
        }*/
/*
        List<Comment> comments = commentMapper.selectByTarget(2,12,132);
        for(Comment comment : comments) {
            System.out.println(comment.getId()+"  "+comment.getContent());
        }*/

        System.out.println(commentMapper.selectCountByEntity(1,228));

    }


}
