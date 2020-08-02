package cool.yzt.cap.service;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.User;

import java.util.List;

public interface FollowService {
    void follow(int followerId,int followedId);
    void unFollow(int followerId,int followedId);
    boolean isFollow(int followerId,int followedId);
    PageBean findFollower(int targetUserId, int selfId, int start,int limit);
    PageBean findFollowing(int targetUserId, int selfId, int start,int limit);
    long findFollowerCount(int userId);
    long findFollowedCount(int userId);
}
