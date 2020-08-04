package cool.yzt.cap.service;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.SystemNotice;

import java.util.Map;

public interface SystemNoticeService {
    int save(SystemNotice notice);
    Map<String,Map<String,Object>> getNoticeList(int userId);
    int findAllUnreadCount(int userId);
    int findAllCountByType(int userId,int type);
    PageBean getNoticeDetailPage(int userId,int type,int start,int limit);
    int updateStatus(int userId,int type);
}
