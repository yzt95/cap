package cool.yzt.cap.service.impl;

import cool.yzt.cap.service.FollowService;

import java.util.List;

/**
 * @author:yzt
 * @description:
 */
public class FollowServiceImpl implements FollowService {
    @Override
    public void follow(int userId, int entityType, int followedId) {
    }

    @Override
    public List<Integer> findFollower(int userId) {
        return null;
    }

    @Override
    public List<Integer> findFollowed(int userId) {
        return null;
    }

    @Override
    public int findFollowerCount(int userId) {
        return 0;
    }

    @Override
    public int findFollowedCount(int userId) {
        return 0;
    }
}
