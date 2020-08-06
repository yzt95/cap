package cool.yzt.cap;

import cool.yzt.cap.entity.StatisticData;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.StatisticService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author : yzt
 */
@SpringBootTest
public class StatisticTest {
    @Autowired
    private StatisticService statisticService;

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        Date date = new Date();
        System.out.println(date);
        date = DateUtil.getYMD(date);
        System.out.println(date);
    }

    @Test
    public void test2() {
        Date from = new Date(System.currentTimeMillis()-1000*3600*24);
        //Date to= new Date();
        //from = DateUtil.getYMD(from);
        //to = DateUtil.getYMD(to);
        Date date = new Date();
        date = DateUtil.getYMD(date);
        //int uv = 100;
        //int dau = 50;
        //statisticService.save(100,50,date);
        StatisticData data=  statisticService.findByDate(from);
        System.out.println(data);
        /*
        List<StatisticData> list = statisticService.findForPeriod(from,to);
        for (StatisticData data : list) {
            System.out.println(data);
        }

         */

    }

    @Test
    public void test3() {
        User user = userService.findById(156);
        System.out.println(user);
    }

}
