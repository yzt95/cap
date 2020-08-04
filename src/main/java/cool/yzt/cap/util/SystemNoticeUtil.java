package cool.yzt.cap.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.entity.SystemNotice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yzt
 */
public class SystemNoticeUtil implements MessageConstant {
    public static Map<String,String> getCommentAndLikeNoticeMap(int userId,
                                                                 int entityType,
                                                                 int entityId,
                                                                 int postTd,
                                                                String username) {
        Map<String,String> map = new HashMap<>();
        map.put("userId",String.valueOf(userId));
        map.put("username",username);
        map.put("entityType",String.valueOf(entityType));
        map.put("entityId",String.valueOf(entityId));
        map.put("postId",String.valueOf(postTd));
        return map;
    }

    public static String getCommentAndLikeNoticeContent(Map<String,String> map) {

        return JSONUtil.toJsonStr(map);
    }

    public static Map<String,String> getFollowNoticeMap(int userId,String username) {
        Map<String,String> map = new HashMap<>();
        map.put("userId",String.valueOf(userId));
        map.put("username",username);
        return map;
    }

    public static String getFollowNoticeContent(Map<String,String> map) {
        return JSONUtil.toJsonStr(map);
    }


    public static String getCommentNoticeMessage(SystemNotice notice) {
        JSONObject jsonObject = JSONUtil.parseObj(notice.getContent());
        String prefix = "用户 ";
        String suffix;
        String who = jsonObject.get("username").toString();
        // 评论
        if (notice.getType() == SYSTEM_NOTICE_TYPE_COMMENT) {
            if (jsonObject.get("entityType").equals("1")) {
                // 评论文章
                suffix = " 评论了您的帖子...";
            } else {
                // 回复评论
                suffix = " 回复了您的评论...";
            }
            // 点赞
        } else if (notice.getType() == SYSTEM_NOTICE_TYPE_LIKE) {
            if (jsonObject.get("entityType").equals("1")) {
                // 点赞文章
                suffix = " 赞了您的帖子...";
            } else {
                // 点赞评论
                suffix = " 赞了您的评论...";
            }
            // 关注
        } else if (notice.getType() == SYSTEM_NOTICE_TYPE_FOLLOW) {
            suffix = " 关注了您...";
        } else {
            suffix = "";
        }
        return prefix + who + suffix;
    }

}
