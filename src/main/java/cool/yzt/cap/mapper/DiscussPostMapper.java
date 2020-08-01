package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {
    List<DiscussPost> selectAllOutsideBlacklist();
    List<DiscussPost> selectAllByUserId(int userId);
    int selectCountOutsideBlacklist();
    DiscussPost selectById(int id);
    int insert(DiscussPost discussPost);
    int updateCommentCount(@Param("postId") int postId, @Param("commentCount") int commentCount);
}