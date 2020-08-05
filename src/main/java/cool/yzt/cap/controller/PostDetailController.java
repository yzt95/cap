package cool.yzt.cap.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.DiscussPost;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.CommentService;
import cool.yzt.cap.service.DiscussPostService;
import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostDetailController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @GetMapping("/post/{postId}")
    public String redirectToPostDetail(@PathVariable("postId") int postId) {
        return ("redirect:/post/"+postId+"/1");
    }

    @GetMapping("/post/{postId}/{commentPage}")
    public String getPostDetailPage(Model model, @PathVariable("postId") int postId, @PathVariable("commentPage") Integer start, Integer pageSize) {

        DiscussPost post = discussPostService.findById(postId);
        int currentUserId = userHolder.get()==null ? 0 : userHolder.get().getId();
        if(post!=null) {
            User user = userService.findById(post.getUserId());
            start = start==null ? 1 : start;
            pageSize = pageSize==null ? 6 : pageSize;
            PageBean pageBean = commentService.getPageBean(postId,start,pageSize,currentUserId);
            model.addAttribute("post",post);
            model.addAttribute("user",user);
            model.addAttribute("likeCount",likeService.findEntityLikeCount(1,post.getId()));
            model.addAttribute("likeStatus",likeService.findEntityLikeStatus(currentUserId,1,post.getId()));
            model.addAttribute("pageBean",pageBean);
        } else {
            return "redirect:/index";
        }
        return "site/discuss-detail";
    }

    @LoginRequired
    @PostMapping("/post/setWonderful")
    @ResponseBody
    public String setWonderful(int postId,int targetStatus) {
        User user = userHolder.get();
        if(user==null || user.getType()<1) {
            return JSONUtil.createObj().set("msg","无权访问").toString();
        }
        int ret = discussPostService.setWonderful(postId,targetStatus);
        if(ret==1) {
            return JSONUtil.createObj().set("code",1).toString();
        }else {
            return JSONUtil.createObj().set("code",0).toString();
        }
    }

    @LoginRequired
    @PostMapping("/post/setTop")
    @ResponseBody
    public String setTop(int postId,int targetType) {
        User user = userHolder.get();
        if(user==null || user.getType()<1) {
            return JSONUtil.createObj().set("msg","无权访问").toString();
        }
        int ret = discussPostService.setTop(postId,targetType);
        if(ret==1) {
            return JSONUtil.createObj().set("code",1).toString();
        }else {
            return JSONUtil.createObj().set("code",0).toString();
        }
    }

    @LoginRequired
    @PostMapping("/post/setDelete")
    @ResponseBody
    public String setDelete(int postId) {
        User user = userHolder.get();
        if(user==null || user.getType()<1) {
            return JSONUtil.createObj().set("msg","无权访问").toString();
        }
        int ret = discussPostService.delete(postId);
        if(ret==1) {
            return JSONUtil.createObj().set("code",1).toString();
        }else {
            return JSONUtil.createObj().set("code",0).toString();
        }
    }








}
