package cool.yzt.cap;

import cn.hutool.extra.mail.MailUtil;
import cool.yzt.cap.util.GeneralUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class UtilTest {
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void test() {
        Context context = new Context();
        context.setVariable("username","yzt");
        context.setVariable("email","839543050@qq.com");
        int userId = 1010;
        int activationCode = 123456;
        String activationUrl = "http://localhost:8080/activation/"+userId+"/"+activationCode;
        context.setVariable("activationUrl",activationUrl);

        String mailContent = templateEngine.process("mail/activation",context);
        System.out.println(mailContent);
        //MailUtil.send("839543050@qq.com","来自 CodeAndPeace 的邮箱验证",mailContent,true);
    }

    @Test
    public void testMD5() {
        String str = "123qwe";
        String salt = GeneralUtil.getUUId(5);
        System.out.println(salt);
        String md51 = GeneralUtil.md5(str+salt);
        String md52 = GeneralUtil.md5("123qwe"+salt);
        System.out.println(md51);
        System.out.println(md52);
    }

}

