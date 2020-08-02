package cool.yzt.cap.controller;

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




}
