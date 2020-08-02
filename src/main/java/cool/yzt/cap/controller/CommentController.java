package cool.yzt.cap.controller;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.Comment;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.event.Event;
import cool.yzt.cap.event.EventProducer;
import cool.yzt.cap.service.CommentService;
import cool.yzt.cap.util.SystemNoticeUtil;
import cool.yzt.cap.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.Map;


@Controller
public class CommentController implements MessageConstant {
    @Autowired
    private UserHolder userHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @LoginRequired
    @PostMapping("/comment/add/{postId}")
    public String addComment(Comment comment, @PathVariable("postId") int postId) {
        if(StringUtils.isBlank(comment.getContent())) return ("redirect:/post/"+postId);
        User user = userHolder.get();
        comment.setUserId(user.getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.add(comment);

        Event event = new Event();
        event.setTopic(EVENT_COMMENT);
        event.setTriggerUserId(user.getId());
        event.setTargetUserId(comment.getTargetId());
        Map<String,Integer> data = SystemNoticeUtil.getCommentAndLikeNoticeMap(user.getId()
                ,comment.getEntityType(),comment.getEntityId(),postId);
        event.setData(data);
        eventProducer.triggerEvent(event);
        return ("redirect:/post/"+postId);
    }



}
