package cool.yzt.cap.controller;

import cn.hutool.core.io.IoUtil;
import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.service.LoginTicketService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.GeneralUtil;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController implements MessageConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private LoginTicketService loginTicketService;

    @Value("${path.upload}")
    private String uploadPath;

    @Value("${path.domain}")
    private String domainPath;


    Logger logger = LoggerFactory.getLogger(UserController.class);

    @LoginRequired
    @GetMapping("/setting")
    public String getUserSettingPage() {
        return "site/setting";
    }

    @LoginRequired
    @PostMapping("/setting/uploadHeaderImg")
    public String uploadHeaderImg(MultipartFile headerImage, Model model, HttpServletRequest request, HttpServletResponse response) {
        if(headerImage==null) {
            model.addAttribute("errorMsg","请上传头像文件");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        if(StringUtils.isBlank(fileName)) {
            model.addAttribute("errorMsg","请上传头像文件");
            return "/site/setting";
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix) || !(suffix.equals(".png")||suffix.equals(".jpg")||suffix.equals(".jpeg"))) {
            model.addAttribute("errorMsg","请上传图片文件");
            return "/site/setting";
        }

        fileName = GeneralUtil.getUUId()+suffix;
        File file = new File(uploadPath+fileName);
        System.out.println(uploadPath+fileName);
        try {
            headerImage.transferTo(file);
        } catch (IOException e) {
            logger.error("头像上传失败"+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        String headerUrl = domainPath+request.getContextPath()+"/user/upload/img/"+fileName;
        User user =  userHolder.get();

        int result = userService.changeHeader(user.getId(),headerUrl);

        if(result==1) {
            return "redirect:/index";
        }else {
            model.addAttribute("errorMsg","修改头像失败，请重试");
            return "/site/setting";
        }
    }

    @GetMapping("/upload/img/{imageName}")
    public void getUserUploadImage(@PathVariable("imageName") String imageName,HttpServletResponse response) {
        imageName = uploadPath+imageName;
        String suffix = imageName.substring(imageName.lastIndexOf(".")+1);
        response.setContentType("image/"+suffix);
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(imageName);
            os = response.getOutputStream();
            IoUtil.copy(fis,os);
        } catch (IOException e) {
            logger.error("读取图片失败" + e.getMessage());
        } finally {
            IoUtil.close(fis);
            IoUtil.close(os);
        }
    }

    @LoginRequired
    @PostMapping("/setting/changePassword")
    public String changePassword(String oldPassword, String newPassword, String repeatNewPassword,
                                 Model model, @CookieValue("loginCode") String ticket) {
        if(!GeneralUtil.passwordIsLegal(newPassword)) {
            model.addAttribute("passwordMsg","密码必须包含字母、数字，8-20位");
            return "/site/setting";
        }

        if(!newPassword.equals(repeatNewPassword)) {
            model.addAttribute("repeatPasswordMsg","两次输入的密码不一致!");
            return "/site/setting";
        }

        int id = userHolder.get().getId();
        int result = userService.changePassword(id,oldPassword,newPassword);
        if(result==CHANGE_PASSWORD_FAILED_OLD_IS_WRONG) {
            model.addAttribute("oldPasswordWrongMsg","密码错误，请重新填写！");
            return "/site/setting";
        }

        model.addAttribute("msg","修改密码成功，请重新登录！");
        model.addAttribute("flag",true);
        loginTicketService.logout(ticket);
        return "/site/operate-result";
    }


    @GetMapping("/profile/{username}")
    public String getProfilePage(@PathVariable("username")String username,Model model) {
        User user = userHolder.get();
        User targetUser = userService.findByUsername(username);
        if(user==null) {
            if(targetUser==null) {
                return "error/404";
            }else {
                model.addAttribute("other",true);
                model.addAttribute("loggedIn",false);
                model.addAttribute("user",targetUser);
                model.addAttribute("likeCount",likeService.findUserLikeCount(targetUser.getId()));
                return "site/profile";
            }
        } else {
            model.addAttribute("loggedIn",true);
            if(targetUser==null) {
                return "error/404";
            }else {
               if(user.getUsername().equals(targetUser.getUsername())) {
                   model.addAttribute("other",false);
                   model.addAttribute("user",user);
                   model.addAttribute("likeCount",likeService.findUserLikeCount(user.getId()));
                   return "site/profile";
               }else {
                   model.addAttribute("other",true);
                   model.addAttribute("user",targetUser);
                   model.addAttribute("likeCount",likeService.findUserLikeCount(targetUser.getId()));
                   return "site/profile";
               }
            }
        }
    }
}


