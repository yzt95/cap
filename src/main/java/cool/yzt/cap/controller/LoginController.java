package cool.yzt.cap.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.annotation.LogoutRequired;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.service.LoginTicketService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.GeneralUtil;
import cool.yzt.cap.util.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class LoginController implements MessageConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginTicketService loginTicketService;

    @Autowired
    private UserHolder userHolder;

    @Value("${captcha.with}")
    private int captchaWith;

    @Value("${captcha.height}")
    private int captchaHeight;

    @Value("${login.defaultExpiredTime}")
    private int defaultExpiredTime;

    @Value("${login.rememberMeExpiredTime}")
    private int rememberMeExpiredTime;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @LogoutRequired
    @GetMapping("/login")
    public String getLoginPage() {
        return "site/login";
    }

    @LogoutRequired
    @PostMapping("/login")
    public String login(String username, String password, String captchaCode, boolean rememberMe, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String realCaptchaCode = (String) session.getAttribute("captchaCode");

        if(realCaptchaCode==null || !realCaptchaCode.equalsIgnoreCase(captchaCode)) {
            model.addAttribute("captchaMsg","验证码错误，请重新输入");
            return "site/login";
        }

        String ticket = GeneralUtil.getUUId();
        int expiredSeconds = rememberMe ? rememberMeExpiredTime : defaultExpiredTime;
        int loginResult = userService.login(username, password, expiredSeconds,ticket);

        if(loginResult==LOGIN_FAILED_USERNAME) {
            model.addAttribute("usernameMsg","用户名错误，请重新输入");
            return "site/login";
        }
        if(loginResult==LOGIN_FAILED_STATUS) {
            model.addAttribute("usernameMsg","该账号还未激活，请激活");
            return "site/login";
        }

        if(loginResult==LOGIN_FAILED_PASSWORD) {
            model.addAttribute("passwordMsg","密码错误，请重新输入");
            return "site/login";
        }


        Cookie cookie = new Cookie("loginCode",ticket);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(expiredSeconds);
        response.addCookie(cookie);

        return "redirect:index";
    }


    @LoginRequired
    @GetMapping("/logout")
    public String logout(@CookieValue("loginCode") String ticket) {
        loginTicketService.logout(ticket);
        userHolder.clear();
        return "redirect:index";
    }


    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(captchaWith,captchaHeight);
        String code = captcha.getCode();
        session.setAttribute("captchaCode",code);
        OutputStream os = null;
        os = response.getOutputStream();
        captcha.write(os);
        if(os!=null) os.close();
    }
}
