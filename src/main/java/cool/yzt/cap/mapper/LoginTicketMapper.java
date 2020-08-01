package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginTicketMapper {
    int insert(LoginTicket loginTicket);

    LoginTicket select(String ticket);

    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
