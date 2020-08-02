package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.SystemNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SystemNoticeMapper {
    /**
     * 保存一条系统通知
     */
    int insert(SystemNotice notice);

    /**
     * 根据不同的type查询所有消息
     */
    List<SystemNotice> selectByType(@Param("userId") int userId, @Param("type") int type);

    /**
     * 根据不同的type查询各自的未读消息数
     */
    int selectUnreadCount(@Param("userId") int userId, @Param("type") int type);


    /**
     * 更新未读状态
     */
    int updateStatus(@Param("userId")int userId, @Param("status")int status);

}
