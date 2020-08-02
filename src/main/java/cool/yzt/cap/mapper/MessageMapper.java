package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {
    /**
     * 查询某用户的所有会话，返回会话列表，用于展示会话列表，所以一个会话只包含最后一条信息
     */
    List<Message> selectConversation(int userId);

    /**
     * 查询某用户的所有的未读消息数
     */
    int selectAllUnreadCount(int userId);

    /**
     * 查询某用户的某会话的未读消息数
     */
    int selectUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     * 查询某会话的信息总数
     */
    int selectMessageCount(String conversationId);


    /**
     * 查询某会话的所有信息
     */
    List<Message> selectMessage(String conversationId);

    /**
     * 插入一条新的私信
     */
    int insert(Message message);

    /**
     * 更新未读状态
     */
    int updateStatus(@Param("userId")int userId,@Param("conversationId")String conversationId,@Param("status")int status);

}
