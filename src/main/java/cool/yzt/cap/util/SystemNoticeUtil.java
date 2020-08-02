package cool.yzt.cap.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yzt
 */
public class SystemNoticeUtil {
    public static Map<String,Integer> getCommentAndLikeNoticeMap(int userId,
                                                                 int entityType,
                                                                 int entityId,
                                                                 int postTd) {
        Map<String,Integer> map = new HashMap<>();
        map.put("userId",userId);
        map.put("entityType",entityType);
        map.put("entityId",entityId);
        map.put("postTd",postTd);
        return map;
    }

    public static String getCommentAndLikeNoticeContent(Map<String,Integer> map) {

        return JSONUtil.toJsonStr(map);
    }

    public static Map<String,Integer> getFollowNoticeMap(int userId) {
        Map<String,Integer> map = new HashMap<>();
        map.put("userId",userId);
        return map;
    }

    public static String getFollowNoticeContent(Map<String,Integer> map) {
        return JSONUtil.toJsonStr(map);
    }

}
