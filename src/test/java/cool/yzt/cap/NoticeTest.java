package cool.yzt.cap;

import cool.yzt.cap.service.SystemNoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author : yzt
 */
@SpringBootTest
public class NoticeTest {
    @Autowired
    private SystemNoticeService systemNoticeService;

    @Test
    public void test() {
        Map<String, Map<String, Object>> noticeList = systemNoticeService.getNoticeList(156);

        for(String k : noticeList.keySet()) {
            Map<String, Object> comment  = noticeList.get(k);
            System.out.println(comment.get("unreadCount"));
            System.out.println(comment.get("message"));
            System.out.println(comment.get("time"));
            System.out.println(comment.get("count"));
        }


    }
}
