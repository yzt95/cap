package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
    List<Comment> selectByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);
    int insert(Comment comment);
    int selectCountByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);
    List<Comment> selectByUserId(@Param("userId") int userId,@Param("entityType") int entityType);
}
