package cool.yzt.cap.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.FollowService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.UserHolder;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private UserService userService;


    @ResponseBody
    @PostMapping("/follow")
    public String follow(@Param("followedId") int followedId,@Param("isFollow") boolean isFollow) {
        User user = userHolder.get();
        if(user==null) {
            return JSONUtil.toJsonStr(new JSONObject().set("code",0).set("msg","请先登录"));
        }
        if(user.getId()==followedId) {
            return JSONUtil.toJsonStr(new JSONObject().set("code",0).set("msg","您不可以关注自己哦"));
        }

        if(isFollow) {
            followService.unFollow(user.getId(),followedId);
            return JSONUtil.toJsonStr(new JSONObject().set("code",1).set("msg","取关成功"));
        } else {
            followService.follow(user.getId(),followedId);
            return JSONUtil.toJsonStr(new JSONObject().set("code",1).set("msg","关注成功"));
        }
    }

    @GetMapping("/user/{username}/follower/{page}")
    public String getFollowerList(@PathVariable("username") String username,
                                  @PathVariable("page") Integer start, Integer limit,
                                  Model model) {
        User user = userHolder.get();
        if(user==null) {
            return "site/login";
        }
        start = start==null ? 1 : start;
        limit = limit==null ? 20 : limit;
        PageBean pageBean = new PageBean();
        if(user.getUsername().equals(username)) {
            pageBean = followService.findFollower(user.getId(),user.getId(),start,limit);
            model.addAttribute("isSelf",true);
            model.addAttribute("username",user.getUsername());
        }else {
            User targetUser = userService.findByUsername(username);
            if(targetUser==null) {
                return "redirect:index";
            }else {
                pageBean = followService.findFollower(targetUser.getId(),user.getId(),start,limit);
                model.addAttribute("isSelf",false);
                model.addAttribute("username",targetUser.getUsername());
            }
        }
        model.addAttribute("pageBean",pageBean);
        return "site/follower";
    }

    @GetMapping("/user/{username}/following/{page}")
    public String getFollowingList(@PathVariable("username") String username,
                                  @PathVariable("page") Integer start, Integer limit,
                                  Model model) {
        User user = userHolder.get();
        if(user==null) {
            return "site/login";
        }
        start = start==null ? 1 : start;
        limit = limit==null ? 20 : limit;
        PageBean pageBean = new PageBean();
        if(user.getUsername().equals(username)) {
            pageBean = followService.findFollowing(user.getId(),user.getId(),start,limit);
            model.addAttribute("isSelf",true);
            model.addAttribute("username",user.getUsername());
        }else {
            User targetUser = userService.findByUsername(username);
            if(targetUser==null) {
                return "redirect:index";
            }else {
                pageBean = followService.findFollowing(targetUser.getId(),user.getId(),start,limit);
                model.addAttribute("isSelf",false);
                model.addAttribute("username",targetUser.getUsername());
            }
        }
        model.addAttribute("pageBean",pageBean);
        return "site/followee";
    }





}
