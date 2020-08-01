package cool.yzt.cap.util;

public class RedisKeyUtil {
    /**
     *
     */
    private static final String ENTITY_LIKE_PREFIX = "like:entity";
    private static final String USER_LIKE_PREFIX = "like:user";
    private static final String LOGIN_TICKET_PREFIX = "loginTicket";
    private static final String USER_PREFIX = "user";
    private static final String FOLLOWER_PREFIX = "follower";
    private static final String FOLLOWED_PREFIX = "followed";
    private static final String SPLIT = ":";




    public static String getEntityLikeKey(int entityType,int entityId) {
        return ENTITY_LIKE_PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getUserLikeKey(int userId) {
        return USER_LIKE_PREFIX + SPLIT + userId;
    }

    public static String getLoginTicketKey(String ticket) {
        return LOGIN_TICKET_PREFIX + SPLIT + ticket;
    }

    public static String getUserKey(int userId) {
        return USER_PREFIX + SPLIT + userId;
    }

    public static String getFollowerKey(int userId) {
        return  FOLLOWER_PREFIX + SPLIT + userId;
    }

    public static String getFollowedKey(int userId) {
        return FOLLOWED_PREFIX + SPLIT + userId;
    }
}
