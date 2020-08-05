package cool.yzt.cap.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.LoginTicket;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.UserMapper;
import cool.yzt.cap.service.LoginTicketService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.GeneralUtil;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService, MessageConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketService loginTicketService;


    @Value("${defaultHeaderUrlPrefix}")
    private String defaultHeaderUrlPrefix;

    @Value("${defaultHeaderUrlSuffix}")
    private String defaultHeaderUrlSuffix;

    @Value("${activationUrlPrefix}")
    private String activationUrlPrefix;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User findById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public int register(User user) {
        String salt = GeneralUtil.getUUId(5);
        String password = GeneralUtil.md5(user.getPassword()+salt);

        user.setPassword(password);
        user.setSalt(salt);
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(GeneralUtil.getUUId());
        user.setHeaderUrl(defaultHeaderUrlPrefix+RandomUtil.randomInt(0,1000)+defaultHeaderUrlSuffix);
        user.setCreateTime(new Date());


        userMapper.insert(user);



        /*发送验证邮件*/


        Context context = new Context();
        context.setVariable("username",user.getUsername());
        context.setVariable("email",user.getEmail());
        String activationUrl = activationUrlPrefix+user.getId()+"/"+user.getActivationCode();
        context.setVariable("activationUrl",activationUrl);
        String mailContent = templateEngine.process("mail/activation",context);
        MailUtil.send(user.getEmail(),"来自 CodeAndPeace 的邮箱验证",mailContent,true);


        return REGISTER_SUCCESS;
    }

    @Override
    public int checkRegisterUser(User user) throws RuntimeException {
        if(user == null) {
            logger.error("注册用户为空");
            throw new RuntimeException("注册用户为空");
        }
        Map<Integer,String> msg = new HashMap<>();
        if(StringUtils.isBlank(user.getUsername())) {
            return REGISTER_USERNAME_IS_BLANK;
        }
        if(StringUtils.isBlank(user.getPassword())) {
            return REGISTER_PASSWORD_IS_BLANK;
        }
        if(StringUtils.isBlank(user.getEmail())) {
            return REGISTER_EMAIL_IS_BLANK;
        }

        if(userMapper.selectByUsername(user.getUsername())!=null) {
            return REGISTER_USERNAME_IS_EXIST;
        }
        if(userMapper.selectByEmail(user.getEmail())!=null) {
            return REGISTER_EMAIL_IS_EXIST;
        }
        return REGISTER_SUCCESS;
    }

    @Override
    public int activation(int id, String code) {
        User user = userMapper.selectById(id);
        if(user.getStatus()==1) {
            return ACTIVATION_REPEAT;
        }
        if(user.getActivationCode().equals(code)) {
            userMapper.updateStatus(user.getId(),1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILED;
        }
    }

    @Override
    public int login(String username, String password, long expiredSeconds,String ticket) {
        if(StringUtils.isBlank(username)) {
            return LOGIN_FAILED_USERNAME;
        }
        User user = userMapper.selectByUsername(username);
        if(user==null) {
            return LOGIN_FAILED_USERNAME;
        }
        if(user.getStatus()==0) {
            return LOGIN_FAILED_STATUS;
        }
        String loginPassword = GeneralUtil.md5(password+user.getSalt());
        if(user.getPassword().equals(loginPassword)) {
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUserId(user.getId());
            loginTicket.setTicket(ticket);
            loginTicket.setStatus(1);
            loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*expiredSeconds));
            int ret = loginTicketService.save(loginTicket);
            return LOGIN_SUCCESS;
        }else {
            return LOGIN_FAILED_PASSWORD;
        }
    }

    @Override
    public int changeHeader(int id, String headerUrl) {
        deleteInCache(id);
        return userMapper.updateHeaderUrl(id,headerUrl);
    }

    @Override
    public int changePassword(int id, String oldPassword, String newPassword) {
        User user = userMapper.selectById(id);
        oldPassword = GeneralUtil.md5(oldPassword+user.getSalt());
        if(oldPassword.equals(user.getPassword())) {
            deleteInCache(id);
            userMapper.updatePassword(id,GeneralUtil.md5(newPassword+user.getSalt()));
            return CHANGE_PASSWORD_SUCCESS;
        }
        return CHANGE_PASSWORD_FAILED_OLD_IS_WRONG;
    }

    @Override
    public User findInCacheById(int id) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getUserKey(id);
        String userJson = jedis.get(key);
        if(StringUtils.isNotBlank(userJson)) {
            User user = JSONUtil.toBean(userJson,User.class);
            RedisUtil.close(jedis);
            return user;
        } else {
            RedisUtil.close(jedis);
            return null;
        }
    }

    @Override
    public void saveInCache(User user) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getUserKey(user.getId());
        String value = JSONUtil.toJsonStr(user);
        Transaction multi = jedis.multi();
        try {
            multi.set(key,value);
            multi.expire(key,3600);
            multi.exec();
        } catch (Exception e) {
            multi.discard();
        }
        RedisUtil.close(jedis);
    }

    @Override
    public void deleteInCache(int id) {
        Jedis jedis = RedisUtil.getJedis();
        String key = RedisKeyUtil.getUserKey(id);
        jedis.del(key);
        RedisUtil.close(jedis);
    }
}
