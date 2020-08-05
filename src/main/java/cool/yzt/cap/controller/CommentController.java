package cool.yzt.cap.controller;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.Comment;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.event.Event;
import cool.yzt.cap.event.EventProducer;
import cool.yzt.cap.service.CommentService;
import cool.yzt.cap.task.PostScoreTask;
import cool.yzt.cap.util.SystemNoticeUtil;
import cool.yzt.cap.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
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

    @Autowired
    private PostScoreTask postScoreTask;

    @LoginRequired
    @PostMapping("/comment/add/{postId}")
    public String addComment(Comment comment, @PathVariable("postId") int postId, @Param("authorId") int authorId) {
        if(StringUtils.isBlank(comment.getContent())) return ("redirect:/post/"+postId);
        User user = userHolder.get();
        comment.setUserId(user.getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.add(comment);

        if(user.getId()!=authorId) {
            Event event = new Event();
            event.setTopic(EVENT_COMMENT);
            event.setTriggerUserId(user.getId());
            event.setTargetUserId(authorId);
            Map<String,String> data = SystemNoticeUtil.getCommentAndLikeNoticeMap(user.getId()
                    ,comment.getEntityType(),comment.getEntityId(),postId,user.getUsername());
            event.setData(data);
            eventProducer.triggerEvent(event);
        }
        postScoreTask.recordPostId(postId);
        return ("redirect:/post/"+postId);
    }



}
