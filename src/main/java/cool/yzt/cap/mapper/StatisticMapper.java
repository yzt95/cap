package cool.yzt.cap.mapper;

import cool.yzt.cap.entity.StatisticData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface StatisticMapper {
    int insert(@Param("date") Date date, @Param("uv") long uv, @Param("dau") long dau);
    StatisticData selectByDate(@Param("date") Date date);
    List<StatisticData> selectForPeriod(@Param("from")Date from, @Param("to")Date to);
}
