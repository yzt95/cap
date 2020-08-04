package cool.yzt.cap.service.impl;


import cn.hutool.db.Page;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cool.yzt.cap.constant.MessageConstant;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.Message;
import cool.yzt.cap.entity.SystemNotice;
import cool.yzt.cap.mapper.SystemNoticeMapper;
import cool.yzt.cap.service.SystemNoticeService;
import cool.yzt.cap.util.PageBeanUtil;
import cool.yzt.cap.util.SystemNoticeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yzt
 */
@Service
public class SystemNoticeServiceImpl implements SystemNoticeService, MessageConstant {
    @Autowired
    private SystemNoticeMapper systemNoticeMapper;

    @Override
    public int save(SystemNotice notice) {
        return systemNoticeMapper.insert(notice);
    }

    @Override
    public int findAllUnreadCount(int userId) {
        return systemNoticeMapper.selectAllUnreadCount(userId);
    }

    @Override
    public Map<String, Map<String, Object>> getNoticeList(int userId) {
        Map<String, Map<String, Object>> contents = new HashMap<>();

        Map<String,Object> content = new HashMap<>();
        SystemNotice notice = systemNoticeMapper.selectLastByType(userId,SYSTEM_NOTICE_TYPE_COMMENT);
        if(notice==null) {
            contents.put("commentNotice",null);
        } else {
            content.put("unreadCount",systemNoticeMapper.selectUnreadCount(userId,SYSTEM_NOTICE_TYPE_COMMENT));
            content.put("time",notice.getCreateTime());
            content.put("count",systemNoticeMapper.selectAllCountByType(userId,SYSTEM_NOTICE_TYPE_COMMENT));
            content.put("message",SystemNoticeUtil.getCommentNoticeMessage(notice));
            contents.put("commentNotice",content);
        }

        notice = systemNoticeMapper.selectLastByType(userId,SYSTEM_NOTICE_TYPE_LIKE);
        if(notice==null) {
            contents.put("likeNotice",null);
        } else {
            content = new HashMap<>();
            content.put("unreadCount",systemNoticeMapper.selectUnreadCount(userId,SYSTEM_NOTICE_TYPE_LIKE));
            content.put("time",notice.getCreateTime());
            content.put("count",systemNoticeMapper.selectAllCountByType(userId,SYSTEM_NOTICE_TYPE_LIKE));
            content.put("message",SystemNoticeUtil.getCommentNoticeMessage(notice));
            contents.put("likeNotice",content);
        }

        notice = systemNoticeMapper.selectLastByType(userId,SYSTEM_NOTICE_TYPE_FOLLOW);
        if(notice==null) {
            contents.put("followNotice",null);
        } else {
            content = new HashMap<>();
            content.put("unreadCount",systemNoticeMapper.selectUnreadCount(userId,SYSTEM_NOTICE_TYPE_FOLLOW));
            content.put("time",notice.getCreateTime());
            content.put("count",systemNoticeMapper.selectAllCountByType(userId,SYSTEM_NOTICE_TYPE_FOLLOW));
            content.put("message",SystemNoticeUtil.getCommentNoticeMessage(notice));
            contents.put("followNotice",content);
        }
        return contents;
    }

    @Override
    public int findAllCountByType(int userId, int type) {
        return systemNoticeMapper.selectAllCountByType(userId,type);
    }

    @Override
    public PageBean getNoticeDetailPage(int userId, int type, int start, int limit) {
        PageHelper.startPage(start,limit);
        List<SystemNotice> notices = systemNoticeMapper.selectByType(userId,type);
        PageInfo<SystemNotice> pageInfo = new PageInfo<>(notices);
        List<Map<String, Object>> contents = new ArrayList<>();
        if(notices!=null&&!notices.isEmpty()) {
            for (SystemNotice notice : notices) {
                Map<String,Object> content = new HashMap<>();
                JSONObject message = JSONUtil.parseObj(notice.getContent());
                content.put("fromUsername",message.get("username"));
                content.put("time",notice.getCreateTime());
                if(notice.getType()!=3) {
                    content.put("postId",message.get("postId"));
                    System.out.println(message.get("postId"));
                }
                contents.add(content);
            }
        }
        return PageBeanUtil.getPageBean(pageInfo,contents);
    }

    @Override
    public int updateStatus(int userId, int type) {
        return systemNoticeMapper.updateStatus(userId,type,1);
    }
}
