package cool.yzt.cap.service;

import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.Message;

public interface MessageService {
    PageBean getMessageList(int userId,int start,int limit);
    int findAllUnreadCount(int userId);
    PageBean getMessageDetail(String conversationId,int start,int limit);

    int add(Message message);
    int updateStatus(int userId,String conversationId,int status);

    boolean userHaveConversation(int userId,String conversationId);

}
