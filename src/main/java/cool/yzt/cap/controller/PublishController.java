package cool.yzt.cap.controller;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.entity.DiscussPost;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.DiscussPostService;
import cool.yzt.cap.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class PublishController {
    @Autowired
    private UserHolder userHolder;

    @Autowired
    private DiscussPostService discussPostService;

    @LoginRequired
    @GetMapping("/publish")
    public String getPublishPage(){
        return "site/publish";
    }

    @LoginRequired
    @PostMapping("/publish")
    public String publish(Model model,
                          @Param("title") String title,
                          @Param("content") String content) {
        User user = userHolder.get();
        if(user==null) return "site/login";

        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
            model.addAttribute("code",0);
            model.addAttribute("msg","请输入完整的标题和内容");
            model.addAttribute("title",title);
            model.addAttribute("content",content);
            return "site/publish";
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setType(0);
        post.setStatus(0);
        post.setCreateTime(new Date());
        post.setCommentCount(0);
        post.setScore(0);

        int postId = discussPostService.save(post);

        if(postId>0) {
            model.addAttribute("code","1")
                    .addAttribute("msg","帖子发布成功")
                    .addAttribute("checkPost","您可以查看帖子或返回首页")
                    .addAttribute("postId",postId);
        }

        return "site/operate-result";
    }

}
