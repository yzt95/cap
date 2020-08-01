package cool.yzt.cap.controller;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.entity.Comment;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.CommentService;
import cool.yzt.cap.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;


@Controller
public class CommentController {
    @Autowired
    private UserHolder userHolder;


    @Autowired
    private CommentService commentService;

    @LoginRequired
    @PostMapping("/comment/add/{postId}")
    public String addComment(Comment comment, @PathVariable("postId") int postId) {
        if(StringUtils.isBlank(comment.getContent())) return ("redirect:/post/"+postId);
        User user = userHolder.get();
        comment.setUserId(user.getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.add(comment);
        return ("redirect:/post/"+postId);
    }



}
