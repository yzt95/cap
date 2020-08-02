package cool.yzt.cap.service.impl;

import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.Comment;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.CommentMapper;
import cool.yzt.cap.service.CommentService;
import cool.yzt.cap.service.DiscussPostService;
import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.PageBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService, MessageConstant {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Override
    public PageBean getPageBean(int postId, int start, int limit,int currentUserId) {
        PageHelper.startPage(start,limit);
        List<Comment> comments = commentMapper.selectByEntity(COMMENT_ENTITY_TYPE_COMMENT,postId);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        List<Map<String, Object>> commentContents = new ArrayList<>();
        if(comments!=null) {
            for(Comment comment : comments) {
                Map<String,Object> commentContent = new HashMap<>();
                commentContent.put("comment",comment);
                User commentUser = userService.findById(comment.getUserId());
                commentContent.put("user",commentUser);
                commentContent.put("likeCount",likeService.findEntityLikeCount(2,comment.getId()));
                if(currentUserId==0) {
                    commentContent.put("likeStatus",0);
                }else {
                    commentContent.put("likeStatus",likeService.findEntityLikeStatus(currentUserId,2,comment.getId()));
                }
                List<Comment> replies = commentMapper.selectByEntity(COMMENT_ENTITY_TYPE_REPLY,comment.getId());
                List<Map<String,Object>> replyContents = new ArrayList<>();
                if(replies!=null) {
                    for(Comment reply : replies) {
                        Map<String,Object> replyContent = new HashMap<>();
                        replyContent.put("reply",reply);
                        User fromUser = userService.findById(reply.getUserId());
                        replyContent.put("fromUser",fromUser);
                        User toUser = userService.findById(reply.getTargetId());
                        replyContent.put("toUser",toUser);
                        replyContent.put("likeCount",likeService.findEntityLikeCount(2,reply.getId()));
                        if(currentUserId==0) {
                            replyContent.put("likeStatus",0);
                        }else {
                            replyContent.put("likeStatus",likeService.findEntityLikeStatus(currentUserId,2,reply.getId()));
                        }
                        replyContents.add(replyContent);
                    }
                }
                commentContent.put("replies",replyContents);
                commentContents.add(commentContent);
            }
        }
        return PageBeanUtil.getPageBean(pageInfo,commentContents);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int add(Comment comment) throws RuntimeException{
        if(comment==null) {
            throw new RuntimeException("评论不能为空");
        }
        // 转码
        comment.setContent(HtmlUtil.escape(comment.getContent()));

        int ret = commentMapper.insert(comment);

        if(comment.getEntityType()==COMMENT_ENTITY_TYPE_COMMENT) {
            int count = commentMapper.selectCountByEntity(COMMENT_ENTITY_TYPE_COMMENT,comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),count);
        }

        return ret;
    }

    @Override
    public PageBean findByUserId(int userId, int start, int limit) {
        PageHelper.startPage(start,limit);
        List<Comment> comments = commentMapper.selectByUserId(userId,1);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        List<Map<String,Object>> contents = new ArrayList<>();
        if(comments!=null) {
            for(Comment comment : comments) {
                Map<String,Object> content = new HashMap<>();
                content.put("date",comment.getCreateTime());
                content.put("title",discussPostService.findById(comment.getEntityId()).getTitle());
                content.put("content",comment.getContent());
                content.put("postId",comment.getEntityId());
                contents.add(content);
            }
        }
        return PageBeanUtil.getPageBean(pageInfo,contents);
    }
}
