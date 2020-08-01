package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User selectById(int id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    int insert(User user);

    int updateStatus(@Param("id") int id, @Param("status") int status);

    int updateHeaderUrl(@Param("id") int id,@Param("headerUrl") String headerUrl);

    int updatePassword(@Param("id") int id,@Param("password") String password);
}
