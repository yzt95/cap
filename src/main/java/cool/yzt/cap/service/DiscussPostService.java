package cool.yzt.cap.service;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.DiscussPost;

import java.util.List;

public interface DiscussPostService {
    List<DiscussPost> findAllOutsideBlacklistByPageNumber(int start, int limit);
    PageBean getPageBean(int start, int limit);
    int findCountOutsideBlacklist();
    DiscussPost findById(int id);
    int save(DiscussPost post);
    int updateCommentCount(int postId,int commentCount);
    PageBean findByUserId(int userId,int start, int limit);

    int setTop(int postId,int type);
    int setWonderful(int postId,int status);
    int delete(int postId);

}
