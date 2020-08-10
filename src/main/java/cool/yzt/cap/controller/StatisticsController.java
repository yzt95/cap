package cool.yzt.cap.controller;

import cool.yzt.cap.annotation.LoginRequired;
import cool.yzt.cap.entity.StatisticData;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.service.StatisticService;
import cool.yzt.cap.util.DateUtil;
import cool.yzt.cap.util.UserHolder;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.management.ObjectName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author : yzt
 */
@Controller
public class StatisticsController {
    @Autowired
    private UserHolder userHolder;

    @Autowired
    private StatisticService statisticService;

    @LoginRequired
    @RequestMapping("/admin")
    public String getStatisticsPage(@DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
                                    Model model) {
        User user = userHolder.get();
        if (user == null || user.getType() != 2) {
            return "forward:/index";
        }
        System.out.println(from);
        System.out.println(to);
        Date yesterday = DateUtil.getYMD(new Date());
        Date oneWeekAgo = DateUtil.getYMD(new Date(System.currentTimeMillis()-1000*3600*24*7));
        from = from==null ? oneWeekAgo : from ;
        to = to==null ? yesterday :to;
        List<StatisticData> list = statisticService.findForPeriod(from,to);
        model.addAttribute("data",list);
        return "site/admin/data";
    }
}
