package cool.yzt.cap.controller;

import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;


@Controller
public class RegisterController implements MessageConstant {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    @PostMapping("/register")
    public String register(Model model, User user, String repeatPassword) {
        if(!GeneralUtil.usernameIsLegal(user.getUsername())) {
            model.addAttribute("usernameMsg","5-16位，可以包含字母，数字，下划线");
            return "site/register";
        }
        if(!GeneralUtil.passwordIsLegal(user.getPassword())) {
            model.addAttribute("passwordMsg","密码必须包含字母、数字，8-20位");
            return "site/register";
        }

        if(!user.getPassword().equals(repeatPassword)) {
            model.addAttribute("repeatPasswordMsg","两次输入的密码不一致!");
            return "site/register";
        }

        int checkRegisterResult = userService.checkRegisterUser(user);

        if(checkRegisterResult==REGISTER_SUCCESS) {
            int registerResult = userService.register(user);
            if(registerResult==REGISTER_SUCCESS) {
                model.addAttribute("msg","您的账号成功注册,请尽快前往注册邮箱激活账号！");
                model.addAttribute("flag",true);
            }
            return "site/operate-result";
        }else {
            if(checkRegisterResult==REGISTER_USERNAME_IS_BLANK) {
                model.addAttribute("usernameMsg","用户名不可以为空！");
            }
            if(checkRegisterResult==REGISTER_USERNAME_IS_EXIST) {
                model.addAttribute("usernameMsg","该用户名已被注册！");
            }
            if(checkRegisterResult==REGISTER_PASSWORD_IS_BLANK) {
                model.addAttribute("passwordMsg","密码不可以为空！");
            }
            if(checkRegisterResult==REGISTER_EMAIL_IS_BLANK) {
                model.addAttribute("emailMsg","邮箱不可以为空！");
            }
            if(checkRegisterResult==REGISTER_EMAIL_IS_EXIST) {
                model.addAttribute("emailMsg","该邮箱已被注册！");
            }
            return "site/register";
        }
    }

    @GetMapping("/activation/{userId}/{activationCode}")
    public String activation(@PathVariable("userId") int userId,@PathVariable("activationCode") String activationCode,Model model) {
        int activationResult = userService.activation(userId,activationCode);
        if(activationResult==ACTIVATION_SUCCESS) {
            model.addAttribute("msg","您的账号成功激活,请登录");
            model.addAttribute("flag",true);
        }else if(activationResult==ACTIVATION_REPEAT) {
            model.addAttribute("msg","激活失败，该账号已经激活");
            model.addAttribute("flag",false);
        }else if(activationResult==ACTIVATION_FAILED) {
            model.addAttribute("msg","激活失败，激活码有误");
            model.addAttribute("flag",false);
        }
        return "site/operate-result";
    }


}
