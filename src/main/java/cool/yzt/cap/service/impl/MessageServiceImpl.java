package cool.yzt.cap.service.impl;

import cn.hutool.core.lang.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cool.yzt.cap.dto.PageBean;

import cool.yzt.cap.entity.Message;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.MessageMapper;
import cool.yzt.cap.mapper.UserMapper;
import cool.yzt.cap.service.MessageService;
import cool.yzt.cap.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageBean getMessageList(int userId, int start, int limit) {
        PageHelper.startPage(start,limit);

        List<Message> messages = messageMapper.selectConversation(userId);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        List<Map<String, Object>> contents = new ArrayList<>();

        if(messages!=null&&!messages.isEmpty()) {
            for(Message message : messages) {
                int friendId = userId==message.getFromId() ? message.getToId() : message.getFromId();
                User friend = userMapper.selectById(friendId);
                Map<String,Object> content = new HashMap<>();

                StringBuilder sb = new StringBuilder();
                if(friendId<userId) {
                    sb.append(friendId).append("_").append(userId);
                }else {
                    sb.append(userId).append("_").append(friendId);
                }
                String conversationId = GeneralUtil.md5(sb.toString());
                //String conversationId = sb.toString();
                content.put("unreadCount",messageMapper.selectUnreadCount(userId,conversationId));
                content.put("count",messageMapper.selectMessageCount(conversationId));
                content.put("friend",friend);
                content.put("message",message);
                contents.add(content);
            }
        }
        PageBean pageBean = new PageBean();
        pageBean.setContents(contents);
        pageBean.setCurrent(pageInfo.getPageNum());
        pageBean.setSize(pageInfo.getPageSize());
        pageBean.setTotalContent(pageInfo.getTotal());
        pageBean.setTotalPage(pageInfo.getPages());
        pageBean.setPrePage(pageInfo.getPrePage());
        pageBean.setNextPage(pageInfo.getNextPage());
        pageBean.setFirst(pageInfo.isIsFirstPage());
        pageBean.setLast(pageInfo.isIsLastPage());
        return pageBean;
    }

    @Override
    public int findAllUnreadCount(int userId) {
        return messageMapper.selectAllUnreadCount(userId);
    }

    @Override
    public PageBean getMessageDetail(String conversationId,int start,int limit) {
        PageHelper.startPage(start,limit);
        List<Message> messages = messageMapper.selectMessage(conversationId);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        List<Map<String, Object>> contents = new ArrayList<>();
        PageBean pageBean = new PageBean();
        pageBean.setContents(contents);
        pageBean.setCurrent(pageInfo.getPageNum());
        pageBean.setSize(pageInfo.getPageSize());
        pageBean.setTotalContent(pageInfo.getTotal());
        pageBean.setTotalPage(pageInfo.getPages());
        pageBean.setPrePage(pageInfo.getPrePage());
        pageBean.setNextPage(pageInfo.getNextPage());
        pageBean.setFirst(pageInfo.isIsFirstPage());
        pageBean.setLast(pageInfo.isIsLastPage());
        if(messages!=null&&!messages.isEmpty()) {
            for(Message message : messages) {
                Map<String,Object> content = new HashMap<>();
                content.put("message",message);
                contents.add(content);
            }
            return pageBean;
        }
        return null;
    }

    @Override
    public int add(Message message) {
        return messageMapper.insert(message);
    }

    @Override
    public int updateStatus(int userId, String conversationId, int status) {
        return messageMapper.updateStatus(userId,conversationId,status);
    }

    @Override
    public boolean userHaveConversation(int userId, String conversationId) {
        List<Message> list = messageMapper.selectConversation(userId);
        for(Message message : list) {
            if(conversationId.equals(message.getConversationId())) return true;
        }
        return false;
    }
}
