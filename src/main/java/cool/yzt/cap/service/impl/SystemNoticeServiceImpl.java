package cool.yzt.cap.service.impl;

import cool.yzt.cap.entity.SystemNotice;
import cool.yzt.cap.mapper.SystemNoticeMapper;
import cool.yzt.cap.service.SystemNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : yzt
 */
@Service
public class SystemNoticeServiceImpl implements SystemNoticeService {
    @Autowired
    private SystemNoticeMapper systemNoticeMapper;

    @Override
    public int save(SystemNotice notice) {
        return systemNoticeMapper.insert(notice);
    }
}
