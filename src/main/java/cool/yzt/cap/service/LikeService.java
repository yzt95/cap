package cool.yzt.cap.service;

public interface LikeService {
    /**
     * 给帖子和评论点赞，同时更新帖子或评论的作者的被点赞数
     * @param userId 点赞的用户
     * @param entityType 点赞的实体的类型：帖子1，评论2
     * @param entityId 点赞的实体id
     * @param  authorId 作者id
     */
    void like(int userId, int entityType, int entityId, int authorId);

    /**
     * 查询某实体的点赞数
     * @param entityType 点赞对象type
     * @param entityId 点赞对象id
     * @return 点赞数
     */
    long findEntityLikeCount(int entityType, int entityId);

    /**
     * 查询用户对某实体是否已点赞
     * @param userId 用户id
     * @param entityType 点赞对象type
     * @param entityId 点赞对象id
     * @return 是否点赞：1点赞，0未点赞
     */
    int findEntityLikeStatus(int userId,int entityType, int entityId);

    /**
     * 查询用户被赞的数量
     * @param userId 用户id
     * @return 被点赞数量
     */
    long findUserLikeCount(int userId);

}
