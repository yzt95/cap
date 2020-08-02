package cool.yzt.cap.service;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.Comment;

import java.util.List;

public interface CommentService {
    public PageBean getPageBean(int postId, int start, int limit,int currentUserId);
    public int add(Comment comment);
    public PageBean findByUserId(int userId, int start, int limit);
}
