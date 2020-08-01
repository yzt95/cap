package cool.yzt.cap.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType,int entityId,int authorId) {
        User user = userHolder.get();
        if(user==null) {
            JSONObject json = JSONUtil.createObj().set("code",0).set("msg","请先登录");
            return json.toString();
        }
        if(user.getId()==authorId) {
            JSONObject json = JSONUtil.createObj().set("code",0).set("msg","您不能赞自己哦");
            return json.toString();
        }
        likeService.like(user.getId(),entityType,entityId,authorId);
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        int likeStatus = likeService.findEntityLikeStatus(user.getId(),entityType,entityId);
        JSONObject json = JSONUtil.createObj()
                .set("code",1)
                .set("msg","点赞成功")
                .set("likeCount",likeCount)
                .set("likeStatus",likeStatus);
        return json.toString();
    }
}
