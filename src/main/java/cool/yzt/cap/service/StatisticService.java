package cool.yzt.cap.service;

import cool.yzt.cap.entity.StatisticData;

import java.util.Date;
import java.util.List;

public interface StatisticService {
    void recordUV(String cookie);
    void recordDAU(int userId);
    void save(long uv,long dau,Date date);
    StatisticData findByDate(Date date);
    List<StatisticData> findForPeriod(Date from,Date to);
}
