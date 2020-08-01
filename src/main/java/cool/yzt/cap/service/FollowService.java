package cool.yzt.cap.service;

import java.util.List;

public interface FollowService {
    void follow(int userId,int entityType,int followedId);
    List<Integer> findFollower(int userId);
    List<Integer> findFollowed(int userId);
    int findFollowerCount(int userId);
    int findFollowedCount(int userId);
}
